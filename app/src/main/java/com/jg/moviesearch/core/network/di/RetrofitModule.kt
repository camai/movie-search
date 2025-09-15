package com.jg.moviesearch.core.network.di

import com.jg.moviesearch.core.network.api.KobisApi
import com.jg.moviesearch.core.network.api.TmdbApi
import com.jg.moviesearch.core.network.di.RetrofitNetwork.createJson
import com.jg.moviesearch.core.network.di.RetrofitNetwork.createRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KobisRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TmdbRetrofit

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    
    @Provides
    @Singleton
    fun provideJson(): Json {
        return createJson()
    }
    
    @Provides
    @Singleton
    @KobisRetrofit
    fun provideKobisRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return createRetrofit(baseUrl = KobisApi.BASE_URL, client = okHttpClient, json = json)
    }
    
    @Provides
    @Singleton
    @TmdbRetrofit
    fun provideTmdbRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        return createRetrofit(baseUrl = TmdbApi.BASE_URL, client = okHttpClient, json = json)
    }
    
    @Provides
    @Singleton
    fun provideKobisApi(@KobisRetrofit retrofit: Retrofit): KobisApi {
        return retrofit.create(KobisApi::class.java)
    }
    
    @Provides
    @Singleton
    fun provideTmdbApi(@TmdbRetrofit retrofit: Retrofit): TmdbApi {
        return retrofit.create(TmdbApi::class.java)
    }
}