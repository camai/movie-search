package com.jg.moviesearch.core.database.di

import com.jg.moviesearch.core.database.dao.FavoriteMovieDao
import com.jg.moviesearch.core.database.db.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideFavoriteMovieDao(
        database: MovieDatabase
    ): FavoriteMovieDao = database.favoriteMovieDao()
}