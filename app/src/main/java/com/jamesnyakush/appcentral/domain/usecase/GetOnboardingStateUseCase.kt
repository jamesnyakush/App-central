package com.jamesnyakush.appcentral.domain.usecase

import com.jamesnyakush.appcentral.domain.model.OnboardingState
import com.jamesnyakush.appcentral.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

class GetOnboardingStateUseCase(
    private val repository: OnboardingRepository
) {

    /**
     * Retrieves the current onboarding state from the repository.
     *
     * @return A Flow that emits the current OnboardingState.
     */
    operator fun invoke(): Flow<OnboardingState> {
        return repository.getOnboardingState()
    }
}