package com.mhacks.app.dagger.component

import android.app.Application
import com.mhacks.app.MHacksApplication
import com.mhacks.app.dagger.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

/**
 * Component that injects into Android members (e.g. Activities and Fragments) with various
 * modules that provide tasks such as networking and caching in a database.
 */
@Singleton
@Component(
        dependencies = [NetComponent::class],
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            ActivityBuilder::class,
            RoomModule::class
        ])
interface AppComponent : AndroidInjector<DaggerApplication> {

    override fun inject(instance: DaggerApplication)

    fun inject(application: MHacksApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun netComponent(netComponent: NetComponent): Builder

        fun roomModule(roomModule: RoomModule): Builder

        fun build(): AppComponent
    }
}
