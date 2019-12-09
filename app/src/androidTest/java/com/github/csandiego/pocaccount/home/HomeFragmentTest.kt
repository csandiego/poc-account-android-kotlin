package com.github.csandiego.pocaccount.home

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.github.csandiego.pocaccount.R
import com.github.csandiego.pocaccount.authentication.AuthenticationContext
import com.github.csandiego.pocaccount.data.UserCredential
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class HomeFragmentTest {

    private lateinit var context: TestAuthenticationContext
    private lateinit var viewModelFactory: ViewModelProvider.Factory

    @Before
    fun setUp() {
        context = TestAuthenticationContext()
        viewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeViewModel(context) as T
            }
        }
    }

    @Test
    fun givenLoggedInWhenFragmentStartedThenDisplayUserId() {
        val id = 1L
        context.mutableUserId.postValue(id)
        launchFragmentInContainer(themeResId = R.style.AppTheme) {
            HomeFragment(viewModelFactory)
        }
        onView(withId(R.id.textViewUserId)).check(matches(withText(id.toString())))
    }

    @Test
    fun givenNotLoggedInWhenFragmentStartedThenNavigateToLoginFragment() {
        val navController = mock(NavController::class.java)
        launchFragmentInContainer(themeResId = R.style.AppTheme) {
            HomeFragment(viewModelFactory).apply {
                viewLifecycleOwnerLiveData.observeForever {
                    if (it != null) {
                        Navigation.setViewNavController(requireView(), navController)
                    }
                }
            }
        }
        verify(navController).navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
    }

    private class TestAuthenticationContext: AuthenticationContext {

        var mutableUserId = MutableLiveData<Long?>()
        override val userId: LiveData<Long?> get() = mutableUserId

        override suspend fun login(credential: UserCredential) = false

        override fun logout() {
        }
    }
}