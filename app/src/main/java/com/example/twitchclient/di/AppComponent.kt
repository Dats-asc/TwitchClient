package com.example.twitchclient.di

import com.example.twitchclient.MyApp
import com.example.twitchclient.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        NetModule::class,
        RepoModule::class,
        ViewModelModule::class,
        ActivityBindsModule::class,
        RoomModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(application: MyApp): Builder

        fun build(): AppComponent
    }

    fun inject(application: MyApp)
}