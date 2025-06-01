package com.jamesnyakush.appcentral.domain.usecase

import com.jamesnyakush.appcentral.domain.repository.OnboardingRepository


class UpdateOnboardingStepUseCase(
    private val repository: OnboardingRepository
) {
    /**
     * Updates the current onboarding step in the repository.
     *
     * @param step The new step to set as the current onboarding step.
     */
    suspend operator fun invoke(step: Int) {
        repository.updateOnboardingStep(step)
    }
}