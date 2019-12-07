package com.github.csandiego.pocaccount.registration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.service.UserRegistrationService
import com.github.csandiego.pocaccount.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegistrationViewModelTest {

    private lateinit var service: TestUserRegistrationService
    private lateinit var viewModel: RegistrationViewModel
    private val credential = UserCredential("someone@somewhere.com", "password")

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        service = TestUserRegistrationService()
        viewModel = RegistrationViewModel(service).apply {
            isEmailValid.observeForever {}
            validationFailure.observeForever {}
            registrationSuccess.observeForever {}
            registrationFailure.observeForever {}
        }
    }

    @Test
    fun whenInitializedThenIsEmailValidIsFalse() {
        assertFalse(viewModel.isEmailValid.value!!)
    }

    @Test
    fun givenAnyEmailWhenEmailSetThenValidateEmail() {
        with(service) {
            validateResult = true
            validateException = false
        }
        viewModel.email = credential.email
        assertEquals(credential.email, service.validateEmail)
    }

    @Test
    fun givenAnyEmailWhenServiceResultInvalidThenIsEmailValidIsFalse() {
        with(service) {
            validateResult = true
            validateException = false
        }
        viewModel.email = credential.email
        service.validateResult = false
        viewModel.email = "prefix" + credential.email
        assertFalse(viewModel.isEmailValid.value!!)
    }

    @Test
    fun givenAnyEmailWhenServiceResultValidThenIsEmailValidIsTrue() {
        with(service) {
            validateResult = true
            validateException = false
        }
        with(viewModel) {
            email = credential.email
            assertTrue(isEmailValid.value!!)
        }
    }

    @Test
    fun givenServiceIssuesWhenValidateThenValidationFailureIsTrue() {
        with(service) {
            validateResult = true
            validateException = true
        }
        with(viewModel) {
            email = credential.email
            assertTrue(validationFailure.value!!)
        }
    }

    @Test
    fun givenValidEmailAndPasswordWhenRegisterThenRegisterUserCredential() {
        with(viewModel) {
            email = credential.email
            password = credential.password
            register()
        }
        assertEquals(credential, service.registerCredential)
    }

    @Test
    fun givenValidEmailAndPasswordWhenRegisterThenRegistrationSuccessIsTrue() {
        with(viewModel) {
            email = credential.email
            password = credential.password
            register()
            assertTrue(registrationSuccess.value!!)
        }
    }

    @Test
    fun givenServiceIssuesWhenRegisterThenRegistrationFailureIsTrue() {
        service.registerException = true
        with(viewModel) {
            email = credential.email
            password = credential.password
            register()
            assertTrue(registrationFailure.value!!)
        }
    }

    private class TestUserRegistrationService : UserRegistrationService {
        lateinit var validateEmail: String
        var validateResult = false
        var validateException = false
        lateinit var registerCredential: UserCredential
        var registerException = false
        override suspend fun validate(email: String): Boolean {
            validateEmail = email
            if (validateException) {
                throw Exception()
            }
            return validateResult
        }
        override suspend fun register(credential: UserCredential) {
            registerCredential = credential
            if (registerException) {
                throw Exception()
            }
        }
    }
}