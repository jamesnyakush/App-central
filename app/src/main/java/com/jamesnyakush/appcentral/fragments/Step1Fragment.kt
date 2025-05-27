package com.jamesnyakush.appcentral.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jamesnyakush.appcentral.OnboardingActivity
import com.jamesnyakush.appcentral.databinding.FragmentStep1Binding
import timber.log.Timber

class Step1Fragment : Fragment() {

    private var _binding: FragmentStep1Binding? = null
    private val binding get() = _binding!!

    private val TAG = "Step1Fragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.continueButton.setOnClickListener {
            Timber.tag(TAG).d("Continue button clicked, navigating to step 2")
            (activity as OnboardingActivity).navigateToStep(2)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}