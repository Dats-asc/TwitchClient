package com.example.twitchclient.di.module

import com.example.twitchclient.ui.auth.AuthFragment
import com.example.twitchclient.ui.auth.AuthModule
import com.example.twitchclient.ui.chat.ChatFragment
import com.example.twitchclient.ui.chat.ChatModule
import com.example.twitchclient.ui.followings.FollowingsFragment
import com.example.twitchclient.ui.followings.FollowingsModule
import com.example.twitchclient.ui.games.GamesFragment
import com.example.twitchclient.ui.games.GamesModule
import com.example.twitchclient.ui.main.MainActivity
import com.example.twitchclient.ui.popular.PopularFragment
import com.example.twitchclient.ui.popular.PopularModule
import com.example.twitchclient.ui.stream.StreamFragment
import com.example.twitchclient.ui.stream.StreamModule
import com.itis.template.di.scope.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBindsModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [AuthModule::class])
    fun contributeAuthFragment(): AuthFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [ChatModule::class])
    fun contributeChatFragment(): ChatFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [FollowingsModule::class])
    fun contributeFollowingsFragment(): FollowingsFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [GamesModule::class])
    fun contributeGamesFragment(): GamesFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [PopularModule::class])
    fun contributePopularFragment(): PopularFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [StreamModule::class])
    fun contributeStreamFragment(): StreamFragment
}