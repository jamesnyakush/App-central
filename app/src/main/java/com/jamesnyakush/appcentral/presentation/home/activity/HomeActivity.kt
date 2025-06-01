package com.jamesnyakush.appcentral.presentation.home.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.jamesnyakush.appcentral.R
import com.jamesnyakush.appcentral.databinding.ActivityHomeBinding
import com.jamesnyakush.appcentral.presentation.home.viewmodel.HomeViewModel
import com.jamesnyakush.appcentral.presentation.onboarding.activity.OnboardingActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeActivity : AppCompatActivity() {

    private val TAG = "HomeActivity"

    private lateinit var binding: ActivityHomeBinding

    // Using Koin for ViewModel injection
    private val viewModel: HomeViewModel by viewModel()

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
        lifecycleScope.launch {
            if (!viewModel.isOnboardingCompleted()) {

                Timber.tag(TAG).d("Onboarding not completed, redirecting")

                val onboardingIntent = Intent(this@HomeActivity, OnboardingActivity::class.java)

                // Pass the current onboarding state
                onboardingIntent.putExtra(
                    "FROM_DEFAULT_LAUNCHER_SETTING",
                    viewModel.onboardingState.value.isDefaultLauncherRequested
                )

                if (intentFlags != 0) {
                    onboardingIntent.addFlags(intentFlags)
                }

                startActivity(onboardingIntent)
            }
        }
    }
}