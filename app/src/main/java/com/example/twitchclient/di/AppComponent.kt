package com.example.twitchclient.di

import com.example.twitchclient.di.module.AppModule
import com.example.twitchclient.di.module.NetModule
import com.example.twitchclient.di.module.RepoModule
import com.example.twitchclient.di.module.ViewModelModule
import com.example.twitchclient.ui.main.MainActivity
import dagger.Component


@Component(
    modules = [
        AppModule::class,
        NetModule::class,
        RepoModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    fun inject(mainActivity: MainActivity)
}