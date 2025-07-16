package com.jg.moviesearch.core.database.di

import android.content.Context
import com.jg.moviesearch.core.database.dao.FavoriteMovieDao
import com.jg.moviesearch.core.database.db.MovieDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 데이터베이스 관련 의존성 주입을 위한 Hilt 모듈
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    companion object {

        /**
         * MovieDatabase 인스턴스
         */
        @Provides
        @Singleton
        fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
            return MovieDatabase.Companion.getDatabase(context)
        }
    }
}