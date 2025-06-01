package com.jamesnyakush.appcentral.domain.usecase

import com.jamesnyakush.appcentral.domain.repository.OnboardingRepository

class CheckDefaultLauncherUseCase(
    private val repository: OnboardingRepository
) {
    /**
     * Checks if the app is set as the default launcher.
     * This will return true if the app is currently the default launcher,
     * otherwise it will return false.
     */
    suspend operator fun invoke(): Boolean {
        return repository.isDefaultLauncher()
    }
}