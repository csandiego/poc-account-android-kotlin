package com.github.csandiego.pocaccount.login

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.csandiego.pocaccount.R
import com.github.csandiego.pocaccount.authentication.AuthenticationContext
import com.github.csandiego.pocaccount.data.UserCredential
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LoginFragmentTest {

    private lateinit var context: TestAuthenticationContext
    private val credential = UserCredential("someone@somewhere.com", "password")

    @Before
    fun setUp() {
        context = TestAuthenticationContext()
        val viewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LoginViewModel(context) as T
            }
        }
        launchFragmentInContainer(themeResId = R.style.AppTheme) {
            LoginFragment(viewModelFactory)
        }
    }

    @Test
    fun givenEmailAndPasswordWhenLoginButtonClickedThenLoginCredential() {
        context.loginException = false
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonLogin)).perform(click())
        assertEquals(credential, context.loginCredential)
    }

    @Test
    fun givenInvalidEmailAndPasswordWhenLoginButtonClickedThenDisplayLoginFailureMessage() {
        with(context) {
            loginUserId = 0L
            loginException = false
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonLogin)).perform(click())
        onView(withText(R.string.login_invalid_credential_message)).check(matches(isDisplayed()))
    }

    @Test
    fun givenLoginIssuesWhenLoginThenDisplayLoginErrorMessage() {
        context.loginException = true
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonLogin)).perform(click())
        onView(withText(R.string.login_error_message)).check(matches(isDisplayed()))
    }

    private class TestAuthenticationContext: AuthenticationContext {
        lateinit var loginCredential: UserCredential
        var loginUserId = 0L
        var loginException = false

        private var _userId = 0L
        private var _isLoggedIn = false

        override val userId: Long
            get() = _userId
        override val isLoggedIn: Boolean
            get() = _isLoggedIn

        override fun login(credential: UserCredential): Boolean {
            loginCredential = credential
            if (loginException) {
                throw Exception()
            }
            if (loginUserId > 0L) {
                _userId = loginUserId
                _isLoggedIn = true
            }
            return isLoggedIn
        }

        override fun logout() {
            _userId = 0L
            _isLoggedIn = false
        }
    }
}