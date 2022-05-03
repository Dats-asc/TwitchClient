package com.example.twitchclient.di.module

import android.content.Context
import com.example.twitchclient.C
import com.example.twitchclient.data.api.TwitchApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named


private val BASE_URL = "https://api.twitch.tv/helix/"
private val AUTH_QUERY_PARAMETER = "Authorization"
private val CLIENT_ID_QUERY_PARAMETER = "Client-Id"

@Module
class NetModule {

    @Provides
    @Named("authInterceptor")
    fun authInterceptor(
        applicationContext: Context
    ): Interceptor = Interceptor { chain ->
        chain.run {
            val updatedRequestUrl = request().url.newBuilder()
                .build()

            proceed(
                request().newBuilder()
                    .url(updatedRequestUrl)
                    .addHeader(CLIENT_ID_QUERY_PARAMETER, C.CLIENT_ID)
                    .addHeader(
                        AUTH_QUERY_PARAMETER,
                        "Bearer ${
                            applicationContext.getSharedPreferences(
                                "USER_PREFERENCES",
                                Context.MODE_PRIVATE
                            ).getString("USER_ACCESS_TOKEN_VALUE", "")
                        }"
                    )
                    .build()
            )
        }
    }

    @Provides
    fun okhttp(
        @Named("authInterceptor") authInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    fun provideGsonConverter(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun api(
        okHttpClient: OkHttpClient,
        provideGsonConverter: GsonConverterFactory
    ): TwitchApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(provideGsonConverter)
            .build()
            .create(TwitchApi::class.java)
}