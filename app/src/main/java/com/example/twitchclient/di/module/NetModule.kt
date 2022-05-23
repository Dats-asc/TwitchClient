package com.example.twitchclient.di.module

import android.content.Context
import android.os.Build
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.twitchclient.BuildConfig
import com.example.twitchclient.C
import com.example.twitchclient.data.api.GraphQLApi
import com.example.twitchclient.data.api.TwitchApi
import com.example.twitchclient.data.api.UsherApi
import com.example.twitchclient.data.responses.usher.VideoPlaylistTokenDeserializer
import com.example.twitchclient.data.responses.usher.VideoPlaylistTokenResponse
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


private val BASE_URL = "https://api.twitch.tv/helix/"
private val AUTH_QUERY_PARAMETER = "Authorization"
private val CLIENT_ID_QUERY_PARAMETER = "Client-Id"

@Module
class NetModule {

    @Provides
    @Singleton
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
    @Singleton
    fun glide(context: Context): RequestManager = Glide.with(context)

    @Provides
    @Singleton
    fun okhttp(
        @Named("authInterceptor") authInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideGsonConverter(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun twitchApi(
        okHttpClient: OkHttpClient,
        provideGsonConverter: GsonConverterFactory
    ): TwitchApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(provideGsonConverter)
            .build()
            .create(TwitchApi::class.java)

    @Singleton
    @Provides
    fun providesUsherApi(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): UsherApi {
        return Retrofit.Builder()
            .baseUrl("https://usher.ttvnw.net/")
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(UsherApi::class.java)
    }

    @Singleton
    @Provides
    fun providesGraphQLApi(
        @Named("gqlclient") client: OkHttpClient,
        @Named("gqlclient") gsonConverterFactory: GsonConverterFactory
    ): GraphQLApi {
        return Retrofit.Builder()
            .baseUrl("https://gql.twitch.tv/gql/")
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(GraphQLApi::class.java)
    }

    @Singleton
    @Provides
    @Named("gqlclient")
    fun providesGQLClient(): OkHttpClient {
        val builder = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                try {


                } catch (e: Exception) {
                    Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2 compatibility", e)
                }
            }
            connectTimeout(5, TimeUnit.MINUTES)
            writeTimeout(5, TimeUnit.MINUTES)
            readTimeout(5, TimeUnit.MINUTES)
        }
        return builder.build()
    }

    @Singleton
    @Provides
    @Named("gqlclient")
    fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(
            GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .registerTypeAdapter(
                    VideoPlaylistTokenResponse::class.java,
                    VideoPlaylistTokenDeserializer()
                )
                .create()
        )
    }
}