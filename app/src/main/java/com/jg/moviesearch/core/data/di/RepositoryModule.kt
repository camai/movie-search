package com.jg.moviesearch.core.data.di

import com.jg.moviesearch.core.data.repository.FavoriteMovieRepositoryImpl
import com.jg.moviesearch.core.data.repository.MovieRepositoryImpl
import com.jg.moviesearch.core.domain.repository.FavoriteMovieRepository
import com.jg.moviesearch.core.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    abstract fun bindFavoriteMovieRepository(
        favoriteMovieRepository: FavoriteMovieRepositoryImpl
    ): FavoriteMovieRepository
}