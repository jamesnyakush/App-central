package com.jamesnyakush.appcentral.domain.repository

import com.jamesnyakush.appcentral.domain.model.OnboardingState
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {

    /**
     * Retrieves the current onboarding state.
     *
     * @return A Flow that emits the current OnboardingState.
     */
    fun getOnboardingState(): Flow<OnboardingState>

    /**
     * Updates the current onboarding step.
     *
     * @param step The new onboarding step to set.
     */
    suspend fun updateOnboardingStep(step: Int)

    /**
     * Marks the onboarding process as completed.
     */
    suspend fun markOnboardingCompleted()

    /**
     * Sets whether the default launcher has been requested.
     *
     * @param requested True if the default launcher has been requested, false otherwise.
     */
    suspend fun setDefaultLauncherRequested(requested: Boolean)

    /**
     * Checks if the app is set as the default launcher.
     *
     * @return True if the app is the default launcher, false otherwise.
     */
    suspend fun isDefaultLauncher(): Boolean
}