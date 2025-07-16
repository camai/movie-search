package com.jg.moviesearch.core.database

import com.jg.moviesearch.core.data.datasource.local.FavoriteDatabaseSource
import com.jg.moviesearch.core.database.dao.FavoriteMovieDao
import com.jg.moviesearch.core.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 즐겨찾기 로컬 데이터베이스 DataSource 구현체
 * 실제 Room DAO를 호출하여 데이터베이스 작업을 수행
 */
@Singleton
class FavoriteDatabaseSourceImpl @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
) : FavoriteDatabaseSource {

    override suspend fun insertFavoriteMovie(favoriteMovie: FavoriteMovieEntity) {
        favoriteMovieDao.insertFavoriteMovie(favoriteMovie)
    }

    override suspend fun deleteFavoriteMovie(favoriteMovie: FavoriteMovieEntity) {
        favoriteMovieDao.deleteFavoriteMovie(favoriteMovie)
    }

    override suspend fun deleteFavoriteMovieByMovieCd(movieCd: String) {
        favoriteMovieDao.deleteFavoriteMovieByMovieCd(movieCd)
    }

    override fun getAllFavoriteMovies(): Flow<List<FavoriteMovieEntity>> {
        return favoriteMovieDao.getAllFavoriteMovies()
    }

    override fun isFavoriteMovie(movieCd: String): Flow<Boolean> {
        return favoriteMovieDao.isFavoriteMovie(movieCd)
    }

    override suspend fun getFavoriteMovieCount(): Int {
        return favoriteMovieDao.getFavoriteMovieCount()
    }

    override suspend fun getFavoriteMovieByMovieCd(movieCd: String): FavoriteMovieEntity? {
        return favoriteMovieDao.getFavoriteMovieByMovieCd(movieCd)
    }
}