package com.github.csandiego.pocaccount.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.github.csandiego.pocaccount.R
import com.github.csandiego.pocaccount.databinding.FragmentRegistrationBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG

class RegistrationFragment(viewModelFactory: ViewModelProvider.Factory) : Fragment() {

    private val viewModel by viewModels<RegistrationViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegistrationBinding.inflate(inflater, container, false).apply {
            viewModel = this@RegistrationFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        with(viewModel) {
            validationFailure.observe(viewLifecycleOwner) {
                if (it) {
                    validationFailureHandled()
                    Snackbar.make(
                        binding.coordinatorLayout,
                        R.string.validation_failure_message,
                        LENGTH_LONG
                    ).show()
                }
            }
            registrationFailure.observe(viewLifecycleOwner) {
                if (it) {
                    registrationFailureHandled()
                    Snackbar.make(
                        binding.coordinatorLayout,
                        R.string.registration_failure_message,
                        LENGTH_LONG
                    ).show()
                }
            }
        }
        return binding.root
    }
}