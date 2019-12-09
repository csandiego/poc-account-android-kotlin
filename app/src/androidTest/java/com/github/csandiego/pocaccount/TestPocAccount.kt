package com.github.csandiego.pocaccount

import com.github.csandiego.pocaccount.dagger.DaggerTestApplicationComponent
import com.github.csandiego.pocaccount.dagger.TestApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class TestPocAccount : PocAccount() {

    lateinit var dagger: TestApplicationComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTestApplicationComponent.factory().create(this).also {
            dagger = it as TestApplicationComponent
        }
    }
}