package com.github.csandiego.pocaccount.home

import androidx.lifecycle.ViewModel
import com.github.csandiego.pocaccount.authentication.AuthenticationContext
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val context: AuthenticationContext) : ViewModel() {

    val userId = context.userId
}