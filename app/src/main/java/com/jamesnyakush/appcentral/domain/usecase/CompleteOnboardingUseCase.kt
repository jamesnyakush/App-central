package com.jamesnyakush.appcentral.domain.usecase

import com.jamesnyakush.appcentral.domain.repository.OnboardingRepository

class CompleteOnboardingUseCase(
    private val repository: OnboardingRepository
) {
    /**
     * Marks the onboarding process as completed.
     * This will update the repository to reflect that the user has finished onboarding.
     */
    suspend operator fun invoke() {
        repository.markOnboardingCompleted()
    }
}