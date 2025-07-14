package com.jg.moviesearch.core.data.di

import com.jg.moviesearch.core.data.network.KobisApi
import com.jg.moviesearch.core.data.network.TmdbApi
import com.jg.moviesearch.core.data.repository.MovieRepositoryImpl
import com.jg.moviesearch.core.domain.repository.MovieRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KobisRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TmdbRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    
    @Provides
    @Singleton
    @KobisRetrofit
    fun provideKobisRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(KobisApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
    
    @Provides
    @Singleton
    @TmdbRetrofit
    fun provideTmdbRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TmdbApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
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
    
    @Provides
    @Singleton
    fun provideMovieRepository(
        kobisApi: KobisApi,
        tmdbApi: TmdbApi
    ): MovieRepository {
        return MovieRepositoryImpl(kobisApi, tmdbApi)
    }
} 