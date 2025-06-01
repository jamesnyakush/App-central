package com.jamesnyakush.appcentral.di

import com.jamesnyakush.appcentral.data.local.PreferencesDataSource
import com.jamesnyakush.appcentral.data.repository.OnboardingRepositoryImpl
import com.jamesnyakush.appcentral.domain.repository.OnboardingRepository
import com.jamesnyakush.appcentral.domain.usecase.CheckDefaultLauncherUseCase
import com.jamesnyakush.appcentral.domain.usecase.CompleteOnboardingUseCase
import com.jamesnyakush.appcentral.domain.usecase.GetOnboardingStateUseCase
import com.jamesnyakush.appcentral.domain.usecase.UpdateOnboardingStepUseCase
import com.jamesnyakush.appcentral.presentation.home.viewmodel.HomeViewModel
import com.jamesnyakush.appcentral.presentation.onboarding.viewmodel.OnboardingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Data sources
    single { PreferencesDataSource(androidContext()) }

    // Repositories
    single<OnboardingRepository> { OnboardingRepositoryImpl(get(), androidContext()) }

    // Use cases
    single { GetOnboardingStateUseCase(get()) }
    single { UpdateOnboardingStepUseCase(get()) }
    single { CompleteOnboardingUseCase(get()) }
    single { CheckDefaultLauncherUseCase(get()) }

    // ViewModels
    viewModel { HomeViewModel(get()) }
    viewModel { OnboardingViewModel(get(), get(), get(), get()) }
}