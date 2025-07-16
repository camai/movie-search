package com.jg.moviesearch.core.data.di

import com.jg.moviesearch.core.data.datasource.network.MovieNetworkSource
import com.jg.moviesearch.core.data.repository.MovieRepositoryImpl
import com.jg.moviesearch.core.domain.repository.MovieRepository
import com.jg.moviesearch.core.network.search.MovieNetworkSourceImpl
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
    abstract fun bindMovieNetworkSource(
        movieNetworkSourceImpl: MovieNetworkSourceImpl
    ): MovieNetworkSource
}