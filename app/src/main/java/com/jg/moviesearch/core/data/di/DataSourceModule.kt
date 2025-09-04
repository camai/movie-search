package com.jg.moviesearch.core.data.di

import com.jg.moviesearch.core.data.datasource.local.FavoriteDatabaseSource
import com.jg.moviesearch.core.data.datasource.network.MovieNetworkSource
import com.jg.moviesearch.core.database.FavoriteDatabaseSourceImpl
import com.jg.moviesearch.core.network.search.MovieNetworkSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindMovieNetworkSource(
        movieNetworkSourceImpl: MovieNetworkSourceImpl
    ): MovieNetworkSource


    @Binds
    abstract fun bindFavoriteDatabaseSource(
        favoriteDatabaseSourceImpl: FavoriteDatabaseSourceImpl
    ): FavoriteDatabaseSource
} 