package com.jamesnyakush.appcentral.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jamesnyakush.appcentral.OnboardingActivity
import com.jamesnyakush.appcentral.databinding.FragmentStep2Binding
import timber.log.Timber

class Step2Fragment : Fragment() {

    private var _binding: FragmentStep2Binding? = null
    private val binding get() = _binding!!

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
            // Save that we're in the middle of setting default launcher
            activity?.getSharedPreferences("home_app_prefs", Context.MODE_PRIVATE)?.edit()
                ?.putBoolean("default_launcher_requested", true)?.apply()

            (activity as OnboardingActivity).requestDefaultLauncher()
        }
    }

    override fun onResume() {
        super.onResume()

        val isDefault = (activity as OnboardingActivity).isDefaultLauncher()
        Timber.tag(TAG).d("onResume: isDefaultLauncher check returned: $isDefault")

        if (isDefault) {
            Timber.tag(TAG).d("App is default launcher, navigating to step 3")
            // Save the current step in case we get interrupted
            activity?.getSharedPreferences("home_app_prefs", Context.MODE_PRIVATE)?.edit()
                ?.putInt("current_onboarding_step", 3)?.apply()
            (activity as OnboardingActivity).navigateToStep(3)
        } else {
            Timber.tag(TAG).d("App is NOT default launcher, staying on step 2")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}