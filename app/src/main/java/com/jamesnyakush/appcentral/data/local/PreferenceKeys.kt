package com.jamesnyakush.appcentral.data.local

/**
 * Preference keys used for storing onboarding and launcher preferences.
 * These keys are used to access shared preferences in the app.
 */
object PreferenceKeys {
    const val PREFERENCES_NAME = "home_app_prefs"
    const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    const val KEY_CURRENT_ONBOARDING_STEP = "current_onboarding_step"
    const val KEY_DEFAULT_LAUNCHER_REQUESTED = "default_launcher_requested"
}