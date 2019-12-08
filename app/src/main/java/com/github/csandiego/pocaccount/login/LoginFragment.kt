package com.github.csandiego.pocaccount.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.github.csandiego.pocaccount.R
import com.github.csandiego.pocaccount.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG

class LoginFragment(viewModelFactory: ViewModelProvider.Factory) : Fragment() {

    private val viewModel by viewModels<LoginViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            viewModel = this@LoginFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        with(viewModel) {
            loginFailure.observe(viewLifecycleOwner) {
                if (it != null) {
                    loginFailureHandled()
                    Snackbar.make(
                        binding.coordinatorLayout,
                        it,
                        LENGTH_LONG
                    ).show()
                }
            }
            loginError.observe(viewLifecycleOwner) {
                if (it) {
                    loginErrorHandled()
                    Snackbar.make(
                        binding.coordinatorLayout,
                        R.string.login_error_message,
                        LENGTH_LONG
                    ).show()
                }
            }
        }
        return binding.root
    }
}