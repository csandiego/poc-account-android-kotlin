package com.github.csandiego.pocaccount.dagger

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.csandiego.pocaccount.home.HomeFragment
import com.github.csandiego.pocaccount.home.HomeViewModel
import com.github.csandiego.pocaccount.login.LoginFragment
import com.github.csandiego.pocaccount.login.LoginViewModel
import com.github.csandiego.pocaccount.registration.RegistrationFragment
import com.github.csandiego.pocaccount.registration.RegistrationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MainActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun provideHomeViewModel(vm: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun provideLoginViewModel(vm: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    fun provideRegistrationViewModel(vm: RegistrationViewModel): ViewModel

    @ActivityScope
    @Binds
    fun provideViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @FragmentKey(HomeFragment::class)
    fun provideHomeFragment(fragment: HomeFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginFragment::class)
    fun provideLoginFragment(fragment: HomeFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RegistrationFragment::class)
    fun provideRegistrationFragment(fragment: RegistrationFragment): Fragment

    @ActivityScope
    @Binds
    fun provideFragmentFactory(factory: DaggerFragmentFactory): FragmentFactory
}