package com.mhacks.app.dagger.module

import com.mhacks.app.ui.main.MainActivity
import com.mhacks.app.ui.welcome.WelcomeFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [
        WelcomeFragmentProvider::class
    ])
    abstract fun bindMainActivity(): MainActivity
}