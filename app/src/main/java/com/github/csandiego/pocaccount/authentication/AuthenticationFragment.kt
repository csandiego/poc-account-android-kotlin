package com.github.csandiego.pocaccount.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.github.csandiego.pocaccount.R
import com.github.csandiego.pocaccount.databinding.FragmentAuthenticationBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG

class AuthenticationFragment(viewModelFactory: ViewModelProvider.Factory) : Fragment() {

    private val viewModel by viewModels<AuthenticationViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAuthenticationBinding.inflate(inflater, container, false).apply {
            viewModel = this@AuthenticationFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        with(viewModel) {
            authenticationFailure.observe(viewLifecycleOwner) {
                if (it) {
                    authenticationFailureHandled()
                    Snackbar.make(
                        binding.coordinatorLayout,
                        R.string.authentication_failure_message,
                        LENGTH_LONG
                    ).show()
                }
            }
        }
        return binding.root
    }
}