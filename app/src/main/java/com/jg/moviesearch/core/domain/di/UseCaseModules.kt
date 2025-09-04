package com.jg.moviesearch.core.domain.di

import com.jg.moviesearch.core.domain.usecase.GetMovieDetailWithFavoriteUseCase
import com.jg.moviesearch.core.domain.usecase.GetMovieDetailWithFavoriteUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModules {
    @Binds
    abstract fun bindGetMovieDetailWithFavoriteUseCase(
        getMovieDetailWithFavoriteUseCase: GetMovieDetailWithFavoriteUseCaseImpl
    ): GetMovieDetailWithFavoriteUseCase
}