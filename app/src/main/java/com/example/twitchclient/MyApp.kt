package com.example.twitchclient

import android.app.Application
import com.example.twitchclient.di.AppComponent
import com.example.twitchclient.di.DaggerAppComponent
import com.example.twitchclient.di.module.AppModule
import com.example.twitchclient.di.module.NetModule

class MyApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(applicationContext))
            .netModule(NetModule())
            .build()
    }
}