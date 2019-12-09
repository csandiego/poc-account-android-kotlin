package com.github.csandiego.pocaccount

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class PocAccountRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application = super.newApplication(cl, TestPocAccount::class.java.name, context)
}