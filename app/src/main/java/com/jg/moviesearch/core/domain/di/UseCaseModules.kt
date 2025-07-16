package com.jg.moviesearch.core.domain.di

import com.jg.moviesearch.core.domain.usecase.GetMovieDetailUseCase
import com.jg.moviesearch.core.domain.usecase.GetMovieDetailUseCaseImpl
import com.jg.moviesearch.core.domain.usecase.GetSearchMovieUseCase
import com.jg.moviesearch.core.domain.usecase.GetSearchMovieUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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

}