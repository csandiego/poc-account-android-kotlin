package com.github.csandiego.pocaccount.home

import androidx.lifecycle.ViewModel
import com.github.csandiego.pocaccount.authentication.AuthenticationContext

class HomeViewModel(private val context: AuthenticationContext) : ViewModel() {

    val userId = context.userId
}