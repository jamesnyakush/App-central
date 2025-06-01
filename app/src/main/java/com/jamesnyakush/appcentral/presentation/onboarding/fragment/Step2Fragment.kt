package com.jamesnyakush.appcentral.presentation.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jamesnyakush.appcentral.databinding.FragmentStep2Binding
import com.jamesnyakush.appcentral.presentation.onboarding.activity.OnboardingActivity
import com.jamesnyakush.appcentral.presentation.onboarding.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import timber.log.Timber

class Step2Fragment : Fragment() {

    private var _binding: FragmentStep2Binding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by activityViewModel()

    private val TAG = "Step2Fragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.continueButton.setOnClickListener {
            Timber.tag(TAG).d("Continue button clicked, requesting default launcher")
            // The ViewModel now handles preferences
            (activity as? OnboardingActivity)?.requestDefaultLauncher()
        }
    }

    override fun onResume() {
        super.onResume()

        // Safely get the activity with null check
        val onboardingActivity = activity as? OnboardingActivity

        if (onboardingActivity == null) {
            Timber.tag(TAG).d("Activity is null or not OnboardingActivity")
            return
        }

        // Use lifecycleScope to call suspend function
        viewLifecycleOwner.lifecycleScope.launch {
            val isDefault = viewModel.isDefaultLauncher()
            Timber.tag(TAG).d("onResume: isDefaultLauncher check returned: $isDefault")

            if (isDefault) {
                Timber.tag(TAG).d("App is default launcher, navigating to step 3")
                // ViewModel now handles preferences
                onboardingActivity.navigateToStep(3)
            } else {
                Timber.tag(TAG).d("App is NOT default launcher, staying on step 2")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}