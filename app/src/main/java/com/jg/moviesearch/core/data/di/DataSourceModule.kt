package com.jg.moviesearch.core.data.di

import com.jg.moviesearch.core.data.datasource.local.FavoriteDatabaseSource
import com.jg.moviesearch.core.data.datasource.network.MovieNetworkSource
import com.jg.moviesearch.core.database.FavoriteDatabaseSourceImpl
import com.jg.moviesearch.core.network.search.MovieNetworkSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * DataSource 관련 의존성 주입을 위한 Hilt 모듈
 * 네트워크 DataSource와 로컬 DataSource를 모두 관리
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    /**
     * MovieNetworkSource 인터페이스를 구현체와 바인딩
     */
    @Binds
    abstract fun bindMovieNetworkSource(
        movieNetworkSourceImpl: MovieNetworkSourceImpl
    ): MovieNetworkSource

    /**
     * FavoriteDatabaseSource 인터페이스를 구현체와 바인딩
     */
    @Binds
    abstract fun bindFavoriteDatabaseSource(
        favoriteDatabaseSourceImpl: FavoriteDatabaseSourceImpl
    ): FavoriteDatabaseSource
} 