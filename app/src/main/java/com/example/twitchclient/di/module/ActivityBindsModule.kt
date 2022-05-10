package com.example.twitchclient.di.module

import com.example.twitchclient.ui.main.MainActivity
import com.example.twitchclient.ui.main.MainModule
import com.itis.template.di.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBindsModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    fun contributeMainActivity(): MainActivity
}
