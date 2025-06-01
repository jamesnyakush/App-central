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
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PreferenceKeys.PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    private val _onboardingCompletedFlow = MutableStateFlow(isOnboardingCompleted())
    private val _currentStepFlow = MutableStateFlow(getCurrentStep())
    private val _defaultLauncherRequestedFlow = MutableStateFlow(isDefaultLauncherRequested())

    fun getOnboardingCompletedFlow(): Flow<Boolean> = _onboardingCompletedFlow.asStateFlow()
    fun getCurrentStepFlow(): Flow<Int> = _currentStepFlow.asStateFlow()
    fun getDefaultLauncherRequestedFlow(): Flow<Boolean> = _defaultLauncherRequestedFlow.asStateFlow()

    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(PreferenceKeys.KEY_ONBOARDING_COMPLETED, false)
    }

    fun getCurrentStep(): Int {
        return prefs.getInt(PreferenceKeys.KEY_CURRENT_ONBOARDING_STEP, 1)
    }

    fun isDefaultLauncherRequested(): Boolean {
        return prefs.getBoolean(PreferenceKeys.KEY_DEFAULT_LAUNCHER_REQUESTED, false)
    }

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit { putBoolean(PreferenceKeys.KEY_ONBOARDING_COMPLETED, completed) }
        _onboardingCompletedFlow.value = completed
    }

    fun setCurrentStep(step: Int) {
        prefs.edit { putInt(PreferenceKeys.KEY_CURRENT_ONBOARDING_STEP, step) }
        _currentStepFlow.value = step
    }

    fun setDefaultLauncherRequested(requested: Boolean) {
        prefs.edit { putBoolean(PreferenceKeys.KEY_DEFAULT_LAUNCHER_REQUESTED, requested) }
        _defaultLauncherRequestedFlow.value = requested
    }
}