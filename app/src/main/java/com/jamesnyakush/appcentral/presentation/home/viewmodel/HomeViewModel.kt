package com.jamesnyakush.appcentral.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamesnyakush.appcentral.domain.model.OnboardingState
import com.jamesnyakush.appcentral.domain.usecase.GetOnboardingStateUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


class HomeViewModel(
    getOnboardingStateUseCase: GetOnboardingStateUseCase
) : ViewModel() {

    /**
     * StateFlow that holds the current onboarding state.
     * It is initialized with the result of the GetOnboardingStateUseCase.
     * The state will be shared and will only emit updates while the app is subscribed.
     */
    val onboardingState: StateFlow<OnboardingState> = getOnboardingStateUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            OnboardingState(isOnboardingComplete = true)
        )

    /**
     * Checks if the onboarding process has been completed.
     */
    fun isOnboardingCompleted(): Boolean {
        return onboardingState.value.isOnboardingComplete
    }
}