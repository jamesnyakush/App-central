package com.jamesnyakush.appcentral.data.repository

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.jamesnyakush.appcentral.data.local.PreferencesDataSource
import com.jamesnyakush.appcentral.domain.model.OnboardingState
import com.jamesnyakush.appcentral.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class OnboardingRepositoryImpl(
    private val preferencesDataSource: PreferencesDataSource,
    private val context: Context
) : OnboardingRepository {

    /**
     * Retrieves the current onboarding state, combining the completion status,
     * current step, and default launcher request status from the preferences data source.
     *
     * @return A Flow that emits the current OnboardingState.
     */
    override fun getOnboardingState(): Flow<OnboardingState> {
        return combine(
            preferencesDataSource.getOnboardingCompletedFlow(),
            preferencesDataSource.getCurrentStepFlow(),
            preferencesDataSource.getDefaultLauncherRequestedFlow()
        ) { completed, step, requested ->
            OnboardingState(
                isCompleted = completed,
                currentStep = step,
                isDefaultLauncherRequested = requested,
                isOnboardingComplete = true
            )
        }
    }

    /**
     * Updates the current onboarding step in the preferences data source.
     *
     * @param step The new step to set as the current onboarding step.
     */
    override suspend fun updateOnboardingStep(step: Int) {
        preferencesDataSource.setCurrentStep(step)
    }

    /**
     * Marks the onboarding process as completed in the preferences data source.
     */
    override suspend fun markOnboardingCompleted() {
        preferencesDataSource.setOnboardingCompleted(true)
    }

    /**
     * Sets whether the user has requested to set this app as the default launcher.
     *
     * @param requested True if the user has requested to set this app as the default launcher, false otherwise.
     */
    override suspend fun setDefaultLauncherRequested(requested: Boolean) {
        preferencesDataSource.setDefaultLauncherRequested(requested)
    }

    /**
     * Checks if the app is currently set as the default launcher.
     *
     * @return True if the app is the default launcher, false otherwise.
     */
    override suspend fun isDefaultLauncher(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = context.getSystemService(Context.ROLE_SERVICE) as RoleManager
            roleManager.isRoleHeld(RoleManager.ROLE_HOME)
        } else {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)

            val resolveInfo = context.packageManager.resolveActivity(intent, 0)
            resolveInfo?.activityInfo?.packageName == context.packageName
        }
    }
}