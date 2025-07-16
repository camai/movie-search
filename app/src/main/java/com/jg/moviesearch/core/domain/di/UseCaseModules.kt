package com.jg.moviesearch.core.domain.di

import com.jg.moviesearch.core.domain.usecase.GetMovieDetailUseCase
import com.jg.moviesearch.core.domain.usecase.GetMovieDetailUseCaseImpl
import com.jg.moviesearch.core.domain.usecase.GetSearchMovieUseCase
import com.jg.moviesearch.core.domain.usecase.GetSearchMovieUseCaseImpl
import com.jg.moviesearch.core.domain.usecase.AddFavoriteMovieUseCase
import com.jg.moviesearch.core.domain.usecase.AddFavoriteMovieUseCaseImpl
import com.jg.moviesearch.core.domain.usecase.RemoveFavoriteMovieUseCase
import com.jg.moviesearch.core.domain.usecase.GetFavoriteMovieStatusUseCase
import com.jg.moviesearch.core.domain.usecase.GetAllFavoriteMoviesUseCase
import com.jg.moviesearch.core.domain.usecase.GetAllFavoriteMoviesUseCaseImpl
import com.jg.moviesearch.core.domain.usecase.GetFavoriteMovieStatusUseCaseImpl
import com.jg.moviesearch.core.domain.usecase.RemoveFavoriteMovieUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModules {
    @Binds
    abstract fun bindGetMovieSearchUseCase(
        getSearchMovieUseCase: GetSearchMovieUseCaseImpl
    ): GetSearchMovieUseCase

    @Binds
    abstract fun bindGetMovieDetailUseCase(
        getMovieDetailUseCase: GetMovieDetailUseCaseImpl
    ): GetMovieDetailUseCase

    @Binds
    abstract fun bindAddFavoriteMovieUseCase(
        addFavoriteMovieUseCase: AddFavoriteMovieUseCaseImpl
    ): AddFavoriteMovieUseCase

    @Binds
    abstract fun bindRemoveFavoriteMovieUseCase(
        removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCaseImpl
    ): RemoveFavoriteMovieUseCase

    @Binds
    abstract fun bindGetFavoriteMovieStatusUseCase(
        getFavoriteMovieStatusUseCase: GetFavoriteMovieStatusUseCaseImpl
    ): GetFavoriteMovieStatusUseCase

    @Binds
    abstract fun bindGetAllFavoriteMoviesUseCase(
        getAllFavoriteMoviesUseCase: GetAllFavoriteMoviesUseCaseImpl
    ): GetAllFavoriteMoviesUseCase
}