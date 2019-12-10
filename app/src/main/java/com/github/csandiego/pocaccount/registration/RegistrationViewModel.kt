package com.github.csandiego.pocaccount.registration

import androidx.lifecycle.*
import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.service.UserRegistrationService
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegistrationViewModel @Inject constructor(private val service: UserRegistrationService) : ViewModel() {

    val email = MutableLiveData("")
    val password = MutableLiveData("")

    private val _validationError = MutableLiveData(false)
    val validationError: LiveData<Boolean> get() = _validationError

    fun validationErrorHandled() {
        _validationError.value = false
    }

    val isEmailValid = email.switchMap {
        val valid = MutableLiveData(false)
        if (it.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    valid.value = service.validate(it)
                } catch (e: Exception) {
                    _validationError.value = true
                }
            }
        }
        valid
    }

    private val _registrationInProgress = MutableLiveData(false)
    val registrationInProgress: LiveData<Boolean> get() = _registrationInProgress

    private val _registrationSuccess = MutableLiveData(false)
    val registrationSuccess: LiveData<Boolean> get() = _registrationSuccess

    fun registrationSuccessHandled() {
        _registrationSuccess.value = false
    }

    private val _registrationError = MutableLiveData(false)
    val registrationError: LiveData<Boolean> get() = _registrationError

    fun registrationErrorHandled() {
        _registrationError.value = false
    }

    fun register() = viewModelScope.launch {
        _registrationInProgress.value = true
        try {
            service.register(UserCredential(email.value!!, password.value!!))
            _registrationSuccess.value = true
        } catch (e: Exception) {
            _registrationError.value = true
        }
        _registrationInProgress.value = false
    }
}