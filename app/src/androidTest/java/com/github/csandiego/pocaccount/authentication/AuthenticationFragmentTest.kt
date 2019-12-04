package com.github.csandiego.pocaccount.authentication

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.csandiego.pocaccount.R
import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.service.AuthenticationService
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AuthenticationFragmentTest {

    private lateinit var service: TestAuthenticationService
    private val credential = UserCredential("someone@somewhere.com", "password")

    @Before
    fun setUp() {
        service = TestAuthenticationService()
        val viewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AuthenticationViewModel(service) as T
            }
        }
        launchFragmentInContainer(themeResId = R.style.AppTheme) {
            AuthenticationFragment(viewModelFactory)
        }
    }

    @Test
    fun givenEmailAndPasswordWhenAuthenticateButtonClickedThenAuthenticateUserCredential() {
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonAuthenticate)).perform(click())
        assertEquals(credential, service.authenticateCredential)
    }

    @Test
    fun givenServiceIssuesWhenAutheticateButtonClickedThenShowSnackbar() {
        service.authenticateException = true
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonAuthenticate)).perform(click())
        onView(withText(R.string.authentication_failure_message)).check(matches(isDisplayed()))
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