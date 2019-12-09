package com.github.csandiego.pocaccount.dagger

import com.github.csandiego.pocaccount.TestPocAccount
import com.github.csandiego.pocaccount.dao.TestAuthenticationDao
import com.github.csandiego.pocaccount.service.TestAuthenticationService
import com.github.csandiego.pocaccount.service.TestUserRegistrationService
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    TestApplicationModule::class,
    TestDoubleModule::class
])
interface TestApplicationComponent : AndroidInjector<TestPocAccount> {

    fun authenticationDao(): TestAuthenticationDao

    fun userRegistrationService(): TestUserRegistrationService

    fun authenticationService(): TestAuthenticationService

    @Component.Factory
    interface Factory : AndroidInjector.Factory<TestPocAccount>
}