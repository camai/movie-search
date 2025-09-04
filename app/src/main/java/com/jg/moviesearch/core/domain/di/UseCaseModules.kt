package com.jg.moviesearch.core.domain.di

import com.jg.moviesearch.core.domain.usecase.AddFavoriteMovieUseCase
import com.jg.moviesearch.core.domain.usecase.AddFavoriteMovieUseCaseImpl
import com.jg.moviesearch.core.domain.usecase.GetFavoriteMovieStatusUseCase
import com.jg.moviesearch.core.domain.usecase.GetAllFavoriteMoviesUseCase
import com.jg.moviesearch.core.domain.usecase.GetAllFavoriteMoviesUseCaseImpl
import com.jg.moviesearch.core.domain.usecase.GetFavoriteMovieStatusUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModules {

    @Binds
    abstract fun bindAddFavoriteMovieUseCase(
        addFavoriteMovieUseCase: AddFavoriteMovieUseCaseImpl
    ): AddFavoriteMovieUseCase

    @Binds
    abstract fun bindGetFavoriteMovieStatusUseCase(
        getFavoriteMovieStatusUseCase: GetFavoriteMovieStatusUseCaseImpl
    ): GetFavoriteMovieStatusUseCase

    @Binds
    abstract fun bindGetAllFavoriteMoviesUseCase(
        getAllFavoriteMoviesUseCase: GetAllFavoriteMoviesUseCaseImpl
    ): GetAllFavoriteMoviesUseCase
}