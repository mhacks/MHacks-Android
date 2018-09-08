package com.mhacks.app.ui.welcome

import com.mhacks.app.ui.welcome.view.WelcomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Provides dependencies into special blend fragment.
 */
@Module
abstract class WelcomeFragmentProvider {

    @ContributesAndroidInjector(modules = [WelcomeFragmentModule::class])
    abstract fun provideWelcomeFragment(): WelcomeFragment
}