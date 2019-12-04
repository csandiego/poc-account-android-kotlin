package com.github.csandiego.pocaccount.registration

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
import com.github.csandiego.pocaccount.service.UserRegistrationService
import org.hamcrest.Matchers.not
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RegistrationFragmentTest {

    private lateinit var service: TestUserRegistrationService
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
        launchFragmentInContainer(themeResId = R.style.AppTheme) {
            RegistrationFragment(viewModelFactory)
        }
    }

    @Test
    fun givenInValidEmailWhenEmailEnteredThenDisableRegisterButton() {
        service.validateResult = true
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        service.validateResult = false
        onView(withId(R.id.editTextEmail)).perform(replaceText("prefix" + credential.email))
        onView(withId(R.id.buttonRegister)).check(matches(not(isEnabled())))
    }

    @Test
    fun givenValidEmailWhenEmailEnteredThenEnableRegisterButton() {
        service.validateResult = true
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.buttonRegister)).check(matches(isEnabled()))
    }

    @Test
    fun givenValidEmailAndPasswordWhenRegisterButtonClickedThenRegisterUserCredential() {
        service.validateResult = true
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonRegister)).perform(click())
        assertEquals(credential, service.registerCredential)
    }

    @Test
    fun givenServiceIssuesWhenRegisterButtonClickedThenShowSnackbar() {
        with(service) {
            validateResult = true
            registerException = true
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonRegister)).perform(click())
        onView(withText(R.string.registration_failure_message)).check(matches(isDisplayed()))
    }

    private class TestUserRegistrationService : UserRegistrationService {
        var validateResult = false
        lateinit var registerCredential: UserCredential
        var registerException = false
        override suspend fun validate(email: String) = validateResult
        override suspend fun register(credential: UserCredential) {
            registerCredential = credential
            if (registerException) {
                throw Exception()
            }
        }
    }
}