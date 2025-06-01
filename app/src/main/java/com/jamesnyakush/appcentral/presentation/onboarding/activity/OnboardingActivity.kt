package com.jamesnyakush.appcentral.presentation.onboarding.activity

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.jamesnyakush.appcentral.R
import com.jamesnyakush.appcentral.databinding.ActivityOnboardingBinding
import com.jamesnyakush.appcentral.presentation.onboarding.fragment.Step1Fragment
import com.jamesnyakush.appcentral.presentation.onboarding.fragment.Step2Fragment
import com.jamesnyakush.appcentral.presentation.onboarding.fragment.Step3Fragment
import com.jamesnyakush.appcentral.presentation.onboarding.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class OnboardingActivity : AppCompatActivity() {

    private val TAG = "OnboardingActivity"
    private lateinit var binding: ActivityOnboardingBinding
    private val viewModel: OnboardingViewModel by viewModel()

    /**
     * Activity result launcher for requesting the default launcher role.
     * This is used to handle the result when the user sets the app as the default launcher.
     */
    private val roleRequestLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        lifecycleScope.launch {

            if (viewModel.isDefaultLauncher()) {
                Timber.tag(TAG).d("App is now set as default launcher")

                viewModel.navigateToStep(3)
            } else {
                Timber.tag(TAG).d("App is not set as default launcher")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            if (checkAndHandleDefaultLauncherReturn()) return@launch

            val currentStep = viewModel.onboardingState.value.currentStep
            navigateToStep(currentStep)
        }
    }

    /**
     * Handles the case when the activity is launched with a new intent.
     * This is useful for scenarios like returning from the default launcher setting.
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        Timber.tag(TAG).d("onNewIntent called")

        lifecycleScope.launch {
            checkAndHandleDefaultLauncherReturn()
        }
    }

    /**
     * Handles the case when the activity is resumed, checking if the app became the default launcher.
     * If so, it navigates to step 3.
     */
    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val currentStep = viewModel.onboardingState.value.currentStep

            if (currentStep == 2 && viewModel.isDefaultLauncher()) {
                Timber.tag(TAG).d("App became default launcher while in background")
                navigateToStep(3)
            }
        }
    }

    /**
     * Checks if the activity was launched from the default launcher setting and handles navigation.
     * @return true if navigation was handled, false otherwise.
     */
    private suspend fun checkAndHandleDefaultLauncherReturn(): Boolean {
        if (
            intent.getBooleanExtra(
                "FROM_DEFAULT_LAUNCHER_SETTING",
                false
            ) && viewModel.isDefaultLauncher()
        ) {
            Timber.tag(TAG).d("Coming from setting as default launcher, navigating to step 3")

            navigateToStep(3)

            return true
        }
        return false
    }

    /**
     * Navigates to the specified onboarding step.
     * @param step The step number to navigate to (1, 2, or 3).
     */
    fun navigateToStep(step: Int) {
        viewModel.navigateToStep(step)

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

    /**
     * Checks if the app is set as the default launcher.
     * @return true if the app is the default launcher, false otherwise.
     */
    fun requestDefaultLauncher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(ROLE_SERVICE) as RoleManager

            if (roleManager.isRoleAvailable(RoleManager.ROLE_HOME)) {
                // For Android Q and above, we can request the home role directly
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
                roleRequestLauncher.launch(intent)

                Timber.tag(TAG).d("Launched role request for home")
            }
        } else {
            // For older versions, we can use the home intent chooser
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)

            startActivity(intent)

            Timber.tag(TAG).d("Launched home intent chooser")
        }
    }

    /**
     * Completes the onboarding process and finishes the activity.
     * This should be called when the user has completed all onboarding steps.
     */
    fun finishOnboarding() {
        lifecycleScope.launch {
            viewModel.completeOnboarding()
        }
        finish()
    }

}