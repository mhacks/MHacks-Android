package com.mhacks.app.di.common

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDialogFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * A dialog fragment that supports dependency injection
 */
abstract class DaggerDialogFragment: AppCompatDialogFragment(), HasSupportFragmentInjector {

    @Inject lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? = childFragmentInjector
}
