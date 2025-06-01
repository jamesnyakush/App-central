package com.jamesnyakush.appcentral.domain.model

data class OnboardingState(
    val isCompleted: Boolean = false,
    val currentStep: Int = 1,
    val isDefaultLauncherRequested: Boolean = false
)