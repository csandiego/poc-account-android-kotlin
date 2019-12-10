package com.github.csandiego.pocaccount.registration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.service.UserRegistrationService
import com.github.csandiego.pocaccount.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
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
            validationError.observeForever {}
            registrationInProgress.observeForever {}
            registrationSuccess.observeForever {}
            registrationError.observeForever {}
        }
    }

    @Test
    fun givenEmailEmptyWhenEmailSetThenIsEmailValidIsFalse() {
        with(viewModel) {
            email.value = ""
            assertFalse(isEmailValid.value!!)
        }
    }

    @Test
    fun givenAnyEmailWhenEmailSetThenValidateEmail() {
        with(service) {
            validateResult = false
            validateException = false
        }
        viewModel.email.value = credential.email
        assertEquals(credential.email, service.validateEmail)
    }

    @Test
    fun givenAnyEmailWhenServiceResultInvalidThenIsEmailValidIsFalse() {
        with(service) {
            validateResult = false
            validateException = false
        }
        with(viewModel) {
            email.value = credential.email
            assertFalse(isEmailValid.value!!)
        }
    }

    @Test
    fun givenAnyEmailWhenServiceResultValidThenIsEmailValidIsTrue() {
        with(service) {
            validateResult = true
            validateException = false
        }
        with(viewModel) {
            email.value = credential.email
            assertTrue(isEmailValid.value!!)
        }
    }

    @Test
    fun givenServiceIssuesWhenValidateThenValidationFailureIsTrue() {
        with(service) {
            validateResult = false
            validateException = true
        }
        with(viewModel) {
            email.value = credential.email
            assertTrue(validationError.value!!)
        }
    }

    @Test
    fun givenValidEmailAndPasswordWhenRegisterThenRegisterUserCredential() {
        service.registerException = false
        with(viewModel) {
            email.value = credential.email
            password.value = credential.password
            register()
        }
        assertEquals(credential, service.registerCredential)
    }

    @Test
    fun givenValidEmailAndPasswordWhenRegisterThenRegistrationInProgressIsTrue() = mainDispatcherRule.dispatcher.runBlockingTest {
        with(service) {
            registerDelay = 1000L
            registerException = false
        }
        with(viewModel) {
            email.value = credential.email
            password.value = credential.password
            register()
            assertTrue(registrationInProgress.value!!)
        }
    }

    @Test
    fun givenValidEmailAndPasswordWhenRegisterFinishedThenRegistrationInProgressIsFalse() {
        with(service) {
            registerDelay = 0L
            registerException = false
        }
        with(viewModel) {
            email.value = credential.email
            password.value = credential.password
            register()
            assertFalse(registrationInProgress.value!!)
        }
    }

    @Test
    fun givenValidEmailAndPasswordWhenRegisterThenRegistrationSuccessIsTrue() {
        service.registerException = false
        with(viewModel) {
            email.value = credential.email
            password.value = credential.password
            register()
            assertTrue(registrationSuccess.value!!)
        }
    }

    @Test
    fun givenServiceIssuesWhenRegisterThenRegistrationFailureIsTrue() {
        service.registerException = true
        with(viewModel) {
            email.value = credential.email
            password.value = credential.password
            register()
            assertTrue(registrationError.value!!)
        }
    }

    private class TestUserRegistrationService : UserRegistrationService {
        lateinit var validateEmail: String
        var validateResult = false
        var validateException = false
        lateinit var registerCredential: UserCredential
        var registerDelay = 0L
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
            if (registerDelay > 0L) {
                delay(registerDelay)
            }
            if (registerException) {
                throw Exception()
            }
        }
    }
}