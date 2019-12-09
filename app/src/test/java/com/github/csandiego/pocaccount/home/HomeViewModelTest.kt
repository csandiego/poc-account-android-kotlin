package com.github.csandiego.pocaccount.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.csandiego.pocaccount.authentication.AuthenticationContext
import com.github.csandiego.pocaccount.data.UserCredential
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    private lateinit var context: TestAuthenticationContext
    private lateinit var viewModel: HomeViewModel
    private val credential = UserCredential("someone@somewhere.com", "password")

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        context = TestAuthenticationContext()
        viewModel = HomeViewModel(context).apply {
            userId.observeForever {}
        }
    }

    @Test
    fun givenLoggedInWhenGetUserIdThenUserIdEqualToLoggedInUserId() = runBlocking {
        val id = 1L
        with(context) {
            loginUserId = id
            loginException = false
            login(credential)
        }
        assertEquals(id, viewModel.userId.value)
    }

    @Test
    fun givenNotLoggedInWhenGetUserIdThenUserIdNull() {
        assertNull(viewModel.userId.value)
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