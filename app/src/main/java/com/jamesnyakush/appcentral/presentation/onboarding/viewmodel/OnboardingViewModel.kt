package com.jamesnyakush.appcentral.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamesnyakush.appcentral.domain.model.OnboardingState
import com.jamesnyakush.appcentral.domain.usecase.CheckDefaultLauncherUseCase
import com.jamesnyakush.appcentral.domain.usecase.CompleteOnboardingUseCase
import com.jamesnyakush.appcentral.domain.usecase.GetOnboardingStateUseCase
import com.jamesnyakush.appcentral.domain.usecase.UpdateOnboardingStepUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OnboardingViewModel(
    getOnboardingStateUseCase: GetOnboardingStateUseCase,
    private val updateOnboardingStepUseCase: UpdateOnboardingStepUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val checkDefaultLauncherUseCase: CheckDefaultLauncherUseCase
) : ViewModel() {

    /**
     * StateFlow that holds the current onboarding state.
     * It is initialized with the result of the GetOnboardingStateUseCase.
     */
    var onboardingState: StateFlow<OnboardingState> = getOnboardingStateUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            OnboardingState(isOnboardingComplete = true)
        )

    fun navigateToStep(step: Int) {
        viewModelScope.launch {
            updateOnboardingStepUseCase(step)
        }
    }

    /**
     * Completes the onboarding process by calling the completeOnboardingUseCase.
     * This method is called when the user finishes the onboarding steps.
     */
    fun completeOnboarding() {
        viewModelScope.launch {
            completeOnboardingUseCase()
        }
    }

    /**
     * Checks if the app is set as the default launcher.
     * This method uses the CheckDefaultLauncherUseCase to determine the status.
     *
     * @return A boolean indicating whether the app is the default launcher.
     */
    suspend fun isDefaultLauncher(): Boolean {
        return checkDefaultLauncherUseCase()
    }
}