package com.github.csandiego.pocaccount.dagger

import com.github.csandiego.pocaccount.PocAccount
import com.github.csandiego.pocaccount.TestPocAccount
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(includes = [ApplicationModule::class])
interface TestApplicationModule {

    @Singleton
    @Binds
    fun providePocAccount(application: TestPocAccount): PocAccount
}