package com.jamesnyakush.appcentral.domain.model

/**
 * Represents the state of the onboarding process.
 *
 * @property isCompleted Indicates whether the onboarding process has been completed.
 * @property currentStep The current step in the onboarding process.
 * @property isDefaultLauncherRequested Indicates if the user has requested to set this app as the default launcher.
 */
data class OnboardingState(
    var isCompleted: Boolean = false,
    var currentStep: Int = 1,
    var isDefaultLauncherRequested: Boolean = false,
    val isOnboardingComplete: Boolean
)