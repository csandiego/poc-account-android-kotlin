package com.github.csandiego.pocaccount.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.csandiego.pocaccount.R
import com.github.csandiego.pocaccount.authentication.AuthenticationContext
import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    private lateinit var context: TestAuthenticationContext
    private lateinit var viewModel: LoginViewModel
    private val credential = UserCredential("someone@somewhere.com", "password")

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        context =
            TestAuthenticationContext()
        viewModel = LoginViewModel(context).apply {
            loginSuccess.observeForever {}
            loginFailure.observeForever {}
            loginError.observeForever {}
        }
    }

    @Test
    fun givenEmailAndPasswordWhenLoginThenLoginUserCredential() {
        context.loginException = false
        with(viewModel) {
            email = credential.email
            password = credential.password
            login()
        }
        assertEquals(credential, context.loginCredential)
    }

    @Test
    fun givenValidEmailAndPasswordWhenLoginThenLoginSuccessIsTrue() {
        with(context) {
            loginUserId = 1L
            loginException = false
        }
        with(viewModel) {
            email = credential.email
            password = credential.password
            login()
            assertTrue(viewModel.loginSuccess.value!!)
        }
    }

    @Test
    fun givenInvalidEmailAndPasswordWhenLoginThenLoginFailureContainsMessage() {
        with(context) {
            loginUserId = 0L
            loginException = false
        }
        with(viewModel) {
            email = credential.email
            password = credential.password
            login()
            assertEquals(R.string.login_invalid_credential_message, loginFailure.value)
        }
    }

    @Test
    fun givenLoginIssuesWhenLoginThenLoginErrorIsTrue() {
        context.loginException = true
        with(viewModel) {
            email = credential.email
            password = credential.password
            login()
            assertTrue(viewModel.loginError.value!!)
        }
    }

    private class TestAuthenticationContext: AuthenticationContext {
        lateinit var loginCredential: UserCredential
        var loginUserId = 0L
        var loginException = false

        private var _userId = MutableLiveData<Long?>()
        override val userId: LiveData<Long?> get() = _userId

        override suspend fun login(credential: UserCredential): Boolean {
            loginCredential = credential
            if (loginException) {
                throw Exception()
            }
            if (loginUserId > 0L) {
                _userId.value = loginUserId
                return true
            }
            return false
        }

        override fun logout() {
            _userId.value = null
        }
    }
}