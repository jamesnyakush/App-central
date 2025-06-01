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

/* *
 * OnboardingViewModel is responsible for managing the state of the onboarding process.
 * It retrieves the current onboarding state, allows navigation to different steps,
 * and handles completion of the onboarding process.
 */
class OnboardingViewModel(
    getOnboardingStateUseCase: GetOnboardingStateUseCase,
    private val updateOnboardingStepUseCase: UpdateOnboardingStepUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val checkDefaultLauncherUseCase: CheckDefaultLauncherUseCase
) : ViewModel() {

    val onboardingState: StateFlow<OnboardingState> = getOnboardingStateUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            OnboardingState()
        )

    fun navigateToStep(step: Int) {
        viewModelScope.launch {
            updateOnboardingStepUseCase(step)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            completeOnboardingUseCase()
        }
    }

    suspend fun isDefaultLauncher(): Boolean {
        return checkDefaultLauncherUseCase()
    }
}