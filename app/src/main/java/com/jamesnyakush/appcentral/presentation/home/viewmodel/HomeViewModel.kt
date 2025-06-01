package com.jamesnyakush.appcentral.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jamesnyakush.appcentral.domain.model.OnboardingState
import com.jamesnyakush.appcentral.domain.usecase.GetOnboardingStateUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/*
 * HomeViewModel is responsible for managing the state of the home screen
 * It retrieves the onboarding state and provides a method to check if onboarding is completed.,
 */
class HomeViewModel(
    getOnboardingStateUseCase: GetOnboardingStateUseCase
) : ViewModel() {

    val onboardingState: StateFlow<OnboardingState> = getOnboardingStateUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5000),
            OnboardingState()
        )

    fun isOnboardingCompleted(): Boolean {
        return onboardingState.value.isCompleted
    }
}