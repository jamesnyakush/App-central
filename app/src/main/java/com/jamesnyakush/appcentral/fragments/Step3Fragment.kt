package com.jamesnyakush.appcentral.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jamesnyakush.appcentral.OnboardingActivity
import com.jamesnyakush.appcentral.databinding.FragmentStep3Binding
import timber.log.Timber


class Step3Fragment : Fragment() {

    private var _binding: FragmentStep3Binding? = null
    private val binding get() = _binding!!

    private val TAG = "Step3Fragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.continueButton.setOnClickListener {
            Timber.tag(TAG).d("Continue button clicked, finishing onboarding")
            (activity as OnboardingActivity).finishOnboarding()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}