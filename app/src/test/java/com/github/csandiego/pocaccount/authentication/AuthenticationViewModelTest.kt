package com.github.csandiego.pocaccount.authentication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.service.AuthenticationService
import com.github.csandiego.pocaccount.test.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthenticationViewModelTest {

    private lateinit var service: TestAuthenticationService
    private lateinit var viewModel: AuthenticationViewModel
    private val credential = UserCredential("someone@somewhere.com", "password")

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        service = TestAuthenticationService()
        viewModel = AuthenticationViewModel(service).apply {
            authenticationSuccess.observeForever {}
            authenticationFailure.observeForever {}
        }
    }

    @Test
    fun givenValidEmailAndPasswordWhenAuthenticateThenAuthenticateUserCredential() {
        with(viewModel) {
            email = credential.email
            password = credential.password
            authenticate()
        }
        assertEquals(credential, service.authenticateCredential)
    }

    @Test
    fun givenValidEmailAndPasswordWhenAuthenticateThenAuthenticationSuccessIsTrue() {
        with(viewModel) {
            email = credential.email
            password = credential.password
            authenticate()
            assertTrue(authenticationSuccess.value!!)
        }
    }

    @Test
    fun givenServiceIssuesWhenAuthenticateThenAuthenticationFailureIsTrue() {
        service.authenticateException = true
        with(viewModel) {
            email = credential.email
            password = credential.password
            authenticate()
            assertTrue(authenticationFailure.value!!)
        }
    }

    private class TestAuthenticationService : AuthenticationService {
        lateinit var authenticateCredential: UserCredential
        var authenticateException = false
        var authenticateUserId = 0L
        override suspend fun authenticate(credential: UserCredential): Long {
            authenticateCredential = credential
            if (authenticateException) {
                throw Exception()
            }
            return authenticateUserId
        }
    }
}