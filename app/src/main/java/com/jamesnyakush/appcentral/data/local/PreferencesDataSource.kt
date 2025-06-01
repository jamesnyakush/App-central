package com.jamesnyakush.appcentral.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.edit

class PreferencesDataSource(
    context: Context
) {
    /**
     * A data source for managing user preferences related to onboarding and default launcher settings.
     * It uses SharedPreferences to store and retrieve these preferences.
     *
     * @param context The application context used to access SharedPreferences.
     */
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PreferenceKeys.PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    // State flows to observe changes in onboarding and launcher preferences
    private val _onboardingCompletedFlow = MutableStateFlow(isOnboardingCompleted())
    private val _currentStepFlow = MutableStateFlow(getCurrentStep())
    private val _defaultLauncherRequestedFlow = MutableStateFlow(isDefaultLauncherRequested())

    /**
     * Provides a Flow to observe changes in the onboarding completion status.
     * @return A Flow that emits the current onboarding completion status.
     */
    fun getOnboardingCompletedFlow(): Flow<Boolean> = _onboardingCompletedFlow.asStateFlow()
    fun getCurrentStepFlow(): Flow<Int> = _currentStepFlow.asStateFlow()
    fun getDefaultLauncherRequestedFlow(): Flow<Boolean> = _defaultLauncherRequestedFlow.asStateFlow()

    /**
     * Checks if the onboarding process has been completed.
     * @return True if onboarding is completed, false otherwise.
     */
    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(PreferenceKeys.KEY_ONBOARDING_COMPLETED, false)
    }

    /**
     * Retrieves the current step in the onboarding process.
     * @return The current step number, defaulting to 1 if not set.
     */
    fun getCurrentStep(): Int {
        return prefs.getInt(PreferenceKeys.KEY_CURRENT_ONBOARDING_STEP, 1)
    }

    /**
     * Checks if the user has requested to set this app as the default launcher.
     * @return True if the default launcher has been requested, false otherwise.
     */
    fun isDefaultLauncherRequested(): Boolean {
        return prefs.getBoolean(PreferenceKeys.KEY_DEFAULT_LAUNCHER_REQUESTED, false)
    }

    /**
     * Sets the onboarding completion status.
     * @param completed True if onboarding is completed, false otherwise.
     */
    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit { putBoolean(PreferenceKeys.KEY_ONBOARDING_COMPLETED, completed) }
        _onboardingCompletedFlow.value = completed
    }

    /**
     * Sets the current step in the onboarding process.
     * @param step The step number to set as the current onboarding step.
     */
    fun setCurrentStep(step: Int) {
        prefs.edit { putInt(PreferenceKeys.KEY_CURRENT_ONBOARDING_STEP, step) }
        _currentStepFlow.value = step
    }

    /**
     * Sets whether the user has requested to set this app as the default launcher.
     * @param requested True if the user has requested to set this app as the default launcher, false otherwise.
     */
    fun setDefaultLauncherRequested(requested: Boolean) {
        prefs.edit { putBoolean(PreferenceKeys.KEY_DEFAULT_LAUNCHER_REQUESTED, requested) }
        _defaultLauncherRequestedFlow.value = requested
    }
}