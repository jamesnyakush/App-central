package com.jamesnyakush.appcentral.data.repository

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.jamesnyakush.appcentral.data.local.PreferencesDataSource
import com.jamesnyakush.appcentral.domain.model.OnboardingState
import com.jamesnyakush.appcentral.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class OnboardingRepositoryImpl(
    private val preferencesDataSource: PreferencesDataSource,
    private val context: Context
) : OnboardingRepository {

    override fun getOnboardingState(): Flow<OnboardingState> {
        return combine(
            preferencesDataSource.getOnboardingCompletedFlow(),
            preferencesDataSource.getCurrentStepFlow(),
            preferencesDataSource.getDefaultLauncherRequestedFlow()
        ) { completed, step, requested ->
            OnboardingState(
                isCompleted = completed,
                currentStep = step,
                isDefaultLauncherRequested = requested,
                isOnboardingComplete = true
            )
        }
    }

    override suspend fun updateOnboardingStep(step: Int) {
        preferencesDataSource.setCurrentStep(step)
    }

    override suspend fun markOnboardingCompleted() {
        preferencesDataSource.setOnboardingCompleted(true)
    }

    override suspend fun setDefaultLauncherRequested(requested: Boolean) {
        preferencesDataSource.setDefaultLauncherRequested(requested)
    }

    override suspend fun isDefaultLauncher(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = context.getSystemService(Context.ROLE_SERVICE) as RoleManager
            roleManager.isRoleHeld(RoleManager.ROLE_HOME)
        } else {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)

            val resolveInfo = context.packageManager.resolveActivity(intent, 0)
            resolveInfo?.activityInfo?.packageName == context.packageName
        }
    }
}