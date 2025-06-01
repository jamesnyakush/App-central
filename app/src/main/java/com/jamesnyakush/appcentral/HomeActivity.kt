package com.jamesnyakush.appcentral

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jamesnyakush.appcentral.databinding.ActivityHomeBinding
import timber.log.Timber


class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkOnboardingStatus()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Timber.tag(TAG).d("onNewIntent called")

        checkOnboardingStatus(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }

    /**
     * Checks if onboarding is completed and redirects to OnboardingActivity if not
     * @param intentFlags Optional flags to add to the intent
     */
    private fun checkOnboardingStatus(intentFlags: Int = 0) {
        val sharedPrefs = getSharedPreferences("home_app_prefs", MODE_PRIVATE)

        val onboardingCompleted = sharedPrefs.getBoolean("onboarding_completed", false)
        val defaultLauncherRequested = sharedPrefs.getBoolean("default_launcher_requested", false)

        if (!onboardingCompleted) {
            Timber.tag(TAG).d("Onboarding not completed, redirecting")

            val onboardingIntent = Intent(this, OnboardingActivity::class.java)
            onboardingIntent.putExtra("FROM_DEFAULT_LAUNCHER_SETTING", defaultLauncherRequested)

            if (intentFlags != 0) {
                onboardingIntent.addFlags(intentFlags)
            }

            startActivity(onboardingIntent)
            return
        }
    }
}