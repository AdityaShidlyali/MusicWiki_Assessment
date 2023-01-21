package com.adityamshidlyali.musicwiki.di

import com.adityamshidlyali.musicwiki.network.EndPoints
import com.adityamshidlyali.musicwiki.network.LastfmApi
import com.adityamshidlyali.musicwiki.utility.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

/**
 * Dagger-Hilt Module for dependency management
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModules {


    /**
     * Provides MusicApi (Lastfm API)
     */
    @Singleton
    @Provides
    fun provideMusicApi(): LastfmApi = Retrofit.Builder()
        .baseUrl(EndPoints.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(LastfmApi::class.java)


    /**
     * Provides the Dispatchers for mocked testing of coroutines
     */
    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}