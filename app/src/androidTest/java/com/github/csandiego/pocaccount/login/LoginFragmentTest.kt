package com.github.csandiego.pocaccount.login

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.github.csandiego.pocaccount.R
import com.github.csandiego.pocaccount.authentication.AuthenticationContext
import com.github.csandiego.pocaccount.data.UserCredential
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class LoginFragmentTest {

    private lateinit var context: TestAuthenticationContext
    private lateinit var scenario: FragmentScenario<LoginFragment>
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
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme) {
            LoginFragment(viewModelFactory)
        }
    }

    @Test
    fun whenEmailEmptyAndPasswordEmptyThenDisableLoginButton() {
        onView(withId(R.id.editTextEmail)).perform(replaceText(""))
        onView(withId(R.id.editTextPassword)).perform(replaceText(""))
        onView(withId(R.id.buttonLogin)).check(matches(not(isEnabled())))
    }

    @Test
    fun whenEmailNotEmptyAndPasswordEmptyThenDisableLoginButton() {
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(""))
        onView(withId(R.id.buttonLogin)).check(matches(not(isEnabled())))
    }

    @Test
    fun whenEmailEmptyAndPasswordNotEmptyThenDisableLoginButton() {
        onView(withId(R.id.editTextEmail)).perform(replaceText(""))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonLogin)).check(matches(not(isEnabled())))
    }

    @Test
    fun whenEmailNotEmptyAndPasswordNotEmptyThenDisableLoginButton() {
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonLogin)).check(matches(isEnabled()))
    }

    @Test
    fun givenEmailAndPasswordWhenLoginButtonClickedThenLoginCredential() {
        with(context) {
            loginUserId = 0L
            loginException = false
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonLogin)).perform(click())
        assertEquals(credential, context.loginCredential)
    }

    @Test
    fun givenValidEmailAndPasswordWhenLoginButtonClickedThenNavigateUp() {
        with(context) {
            loginUserId = 1L
            loginException = false
        }
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonLogin)).perform(click())
        verify(navController).navigateUp()
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
        with(context) {
            loginUserId = 0L
            loginException = true
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonLogin)).perform(click())
        onView(withText(R.string.login_error_message)).check(matches(isDisplayed()))
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