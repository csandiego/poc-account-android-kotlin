package com.github.csandiego.pocaccount

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.csandiego.pocaccount.data.UserCredential
import com.github.csandiego.pocaccount.service.TestAuthenticationService
import com.github.csandiego.pocaccount.service.TestUserRegistrationService
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class E2ETest {

    private lateinit var authenticationService: TestAuthenticationService
    private lateinit var userRegistrationService: TestUserRegistrationService
    private val credential = UserCredential("someone@somewhere.com", "password")

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        with(ApplicationProvider.getApplicationContext<TestPocAccount>().dagger) {
            authenticationService = authenticationService()
            userRegistrationService = userRegistrationService()
        }
    }

    @Test
    fun login() {
        val id = 1L
        with(authenticationService) {
            authenticateUserId = id
            authenticateException = false
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonLogin)).perform(click())
        onView(withId(R.id.textViewUserId)).check(matches(withText(id.toString())))
    }

    @Test
    fun register() {
        val id = 1L
        onView(withId(R.id.buttonRegister)).perform(click())
        with(userRegistrationService) {
            validateResult = true
            validateException = false
            registerException = false
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonRegister)).perform(click())
        with(authenticationService) {
            authenticateUserId = id
            authenticateException = false
        }
        onView(withId(R.id.editTextEmail)).perform(replaceText(credential.email))
        onView(withId(R.id.editTextPassword)).perform(replaceText(credential.password))
        onView(withId(R.id.buttonLogin)).perform(click())
        onView(withId(R.id.textViewUserId)).check(matches(withText(id.toString())))
    }
}