package com.jamesnyakush.appcentral

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
import com.jamesnyakush.appcentral.databinding.ActivityOnboardingBinding
import com.jamesnyakush.appcentral.fragments.Step1Fragment
import com.jamesnyakush.appcentral.fragments.Step2Fragment
import com.jamesnyakush.appcentral.fragments.Step3Fragment
import timber.log.Timber


class OnboardingActivity : AppCompatActivity() {

    private val TAG = "OnboardingActivity"
    private var currentStep = 1

    private val prefs by lazy { getSharedPreferences("home_app_prefs", MODE_PRIVATE) }

    private lateinit var binding: ActivityOnboardingBinding

    private val roleRequestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        savePreference("default_launcher_requested", true)

        if (isDefaultLauncher()) {
            Timber.tag(TAG).d("App is now set as default launcher")
            savePreference("current_onboarding_step", 3)
            navigateToStep(3)
        } else {
            Timber.tag(TAG).d("App is not set as default launcher")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        currentStep = when {
            savedInstanceState != null -> savedInstanceState.getInt("current_step", 1)
            else -> prefs.getInt("current_onboarding_step", 1)
        }

        if (checkAndHandleDefaultLauncherReturn()) return

        navigateToStep(currentStep)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Timber.tag(TAG).d("onNewIntent called")
        checkAndHandleDefaultLauncherReturn()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("current_step", currentStep)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()

        if (currentStep == 2 && isDefaultLauncher()) {
            Timber.tag(TAG).d("App became default launcher while in background")
            navigateToStep(3)
        }
    }

    /**
     * Checks if we're returning from setting the app as default launcher
     * @return true if we've handled the return and navigation
     */
    private fun checkAndHandleDefaultLauncherReturn(): Boolean {
        if (intent.getBooleanExtra("FROM_DEFAULT_LAUNCHER_SETTING", false) && isDefaultLauncher()) {
            Timber.tag(TAG).d("Coming from setting as default launcher, navigating to step 3")
            savePreference("default_launcher_requested", false)
            navigateToStep(3)
            return true
        }
        return false
    }

    /**
     * Helper method to save a boolean preference
     */
    private fun savePreference(key: String, value: Boolean) {
        prefs.edit { putBoolean(key, value) }
    }

    /**
     * Helper method to save an integer preference
     */
    private fun savePreference(key: String, value: Int) {
        prefs.edit { putInt(key, value) }
    }

    fun navigateToStep(step: Int) {
        currentStep = step

        savePreference("current_onboarding_step", step)

        val fragment = when (step) {
            1 -> Step1Fragment()
            2 -> Step2Fragment()
            3 -> Step3Fragment()
            else -> Step1Fragment()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    fun requestDefaultLauncher() {
        savePreference("default_launcher_requested", true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
            if (roleManager.isRoleAvailable(RoleManager.ROLE_HOME)) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
                roleRequestLauncher.launch(intent)
                Timber.tag(TAG).d("Launched role request for home")
            }
        } else {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
            Timber.tag(TAG).d("Launched home intent chooser")
        }
    }

    fun isDefaultLauncher(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            val roleManager = getSystemService(ROLE_SERVICE) as RoleManager
            val isRoleHeld = roleManager.isRoleHeld(RoleManager.ROLE_HOME)

            Timber.tag(TAG).d("isDefaultLauncher check: isRoleHeld = $isRoleHeld")

            isRoleHeld
        } else {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)

            val resolveInfo = packageManager.resolveActivity(intent, 0)
            val isDefault = resolveInfo?.activityInfo?.packageName == packageName

            Timber.tag(TAG).d("isDefaultLauncher check: isDefault = $isDefault, package = ${resolveInfo?.activityInfo?.packageName}, our package = $packageName")
            isDefault
        }
    }

    fun finishOnboarding() {
        savePreference("onboarding_completed", true)
        finish()
    }
}