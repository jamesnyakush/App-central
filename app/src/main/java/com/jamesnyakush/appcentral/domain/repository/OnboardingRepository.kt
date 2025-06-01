package com.jamesnyakush.appcentral.domain.repository

import com.jamesnyakush.appcentral.domain.model.OnboardingState
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {

    fun getOnboardingState(): Flow<OnboardingState>

    suspend fun updateOnboardingStep(step: Int)

    suspend fun markOnboardingCompleted()

    suspend fun setDefaultLauncherRequested(requested: Boolean)

    suspend fun isDefaultLauncher(): Boolean
}