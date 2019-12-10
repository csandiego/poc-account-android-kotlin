package com.github.csandiego.pocaccount.registration

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
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
import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.service.TestUserRegistrationService
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class RegistrationFragmentTest {

    private lateinit var service: TestUserRegistrationService
    private lateinit var scenario: FragmentScenario<RegistrationFragment>
    private val credential = UserCredential("someone@somewhere.com", "password")

    @Before
    fun setUp() {
        service = TestUserRegistrationService()
        val viewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return RegistrationViewModel(service) as T
            }
        }
        scenario = launchFragmentInContainer(themeResId = R.style.AppTheme) {
            RegistrationFragment(viewModelFactory)
        }
    }

    @Test
    fun givenAnyEmailWhenEmailEnteredThenValidateEmail() {
        service.validateException = false
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        scenario.onFragment {}
        assertEquals(service.validateEmail, credential.email)
    }

    @Test
    fun givenServiceIssuesWhenValidateThenShowSnackbar() {
        with(service) {
            validateResult = true
            validateException = true
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        scenario.onFragment {}
        onView(withText(R.string.validation_failure_message)).check(matches(isDisplayed()))
    }

    @Test
    fun whenEmailInvalidAndPasswordEmptyThenDisableRegisterButton() {
        with(service) {
            validateResult = false
            validateException = false
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(""))
        onView(withId(R.id.buttonRegister)).check(matches(not(isEnabled())))
    }

    @Test
    fun whenEmailInvalidAndPasswordNotEmptyThenDisableRegisterButton() {
        with(service) {
            validateResult = false
            validateException = false
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonRegister)).check(matches(not(isEnabled())))
    }

    @Test
    fun whenEmailValidAndPasswordEmptyThenDisableRegisterButton() {
        with(service) {
            validateResult = true
            validateException = false
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(""))
        onView(withId(R.id.buttonRegister)).check(matches(not(isEnabled())))
    }

    @Test
    fun whenEmailValidAndPasswordNotEmptyThenEnableRegisterButton() {
        with(service) {
            validateResult = true
            validateException = false
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonRegister)).check(matches(isEnabled()))
    }

    @Test
    fun givenValidEmailWhenRegisterButtonClickedThenRegisterUserCredential() {
        with(service) {
            validateResult = true
            validateException = false
            registerException = false
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonRegister)).perform(click())
        assertEquals(credential, service.registerCredential)
    }

    @Test
    fun givenValidEmailWhenRegisterButtonClickedThenNavigateUp() {
        with(service) {
            validateResult = true
            validateException = false
            registerException = false
        }
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonRegister)).perform(click())
        verify(navController).navigateUp()
    }

    @Test
    fun givenServiceIssuesWhenRegisterButtonClickedThenShowSnackbar() {
        with(service) {
            validateResult = true
            validateException = false
            registerException = true
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonRegister)).perform(click())
        onView(withText(R.string.registration_failure_message)).check(matches(isDisplayed()))
    }
}