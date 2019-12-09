package com.github.csandiego.pocaccount.dagger

import android.app.Application
import com.github.csandiego.pocaccount.MainActivity
import com.github.csandiego.pocaccount.PocAccount
import com.github.csandiego.pocaccount.authentication.AuthenticationContext
import com.github.csandiego.pocaccount.authentication.DefaultAuthenticationContext
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
interface ApplicationModule {

    @Singleton
    @Binds
    fun provideApplication(application: PocAccount): Application

    @Singleton
    @Binds
    fun provideAuthenticationContext(context: DefaultAuthenticationContext): AuthenticationContext

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    fun contributeMainActivityAndroidInjector(): MainActivity
}