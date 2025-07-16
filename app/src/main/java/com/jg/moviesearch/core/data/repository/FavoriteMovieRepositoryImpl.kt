package com.jg.moviesearch.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.jg.moviesearch.core.domain.repository.FavoriteMovieRepository
import com.jg.moviesearch.core.data.datasource.local.FavoriteDatabaseSource
import com.jg.moviesearch.core.database.entity.FavoriteMovieEntity
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 즐겨찾기 영화 Repository 구현체
 * DataSource를 통해 데이터베이스와 도메인 계층을 연결하는 데이터 계층 구현체
 */
class FavoriteMovieRepositoryImpl @Inject constructor(
    private val favoriteDatabaseSource: FavoriteDatabaseSource
) : FavoriteMovieRepository {
    
    override suspend fun addFavoriteMovie(movie: MovieWithPoster) {
        val favoriteEntity = FavoriteMovieEntity(
            movieCd = movie.movie.movieCd,
            movieNm = movie.movie.movieNm,
            openDt = movie.movie.openDt,
            posterUrl = movie.posterUrl,
            createdAt = System.currentTimeMillis()
        )
        favoriteDatabaseSource.insertFavoriteMovie(favoriteEntity)
    }
    
    override suspend fun removeFavoriteMovie(movieCd: String) {
        favoriteDatabaseSource.deleteFavoriteMovieByMovieCd(movieCd)
    }
    
    override suspend fun toggleFavoriteMovie(movie: MovieWithPoster) {
        val existingFavorite = favoriteDatabaseSource.getFavoriteMovieByMovieCd(movie.movie.movieCd)
        if (existingFavorite != null) {
            removeFavoriteMovie(movie.movie.movieCd)
        } else {
            addFavoriteMovie(movie)
        }
    }
    
    override fun getAllFavoriteMovies(): Flow<List<MovieWithPoster>> {
        return favoriteDatabaseSource.getAllFavoriteMovies().map { entities ->
            entities.map { entity ->
                entity.toMovieWithPoster()
            }
        }
    }
    
    override fun isFavoriteMovie(movieCd: String): Flow<Boolean> {
        return favoriteDatabaseSource.isFavoriteMovie(movieCd)
    }
    
    override suspend fun getFavoriteMovieCount(): Int {
        return favoriteDatabaseSource.getFavoriteMovieCount()
    }
}

/**
 * FavoriteMovieEntity를 MovieWithPoster로 변환하는 확장 함수
 */
private fun FavoriteMovieEntity.toMovieWithPoster(): MovieWithPoster {
    return MovieWithPoster(
        movie = com.jg.moviesearch.core.model.domain.Movie(
            rank = "0",
            rankInten = "0",
            rankOldAndNew = "NEW",
            movieCd = this.movieCd,
            movieNm = this.movieNm,
            openDt = this.openDt,
            salesAmt = "0",
            salesShare = "0.0",
            salesInten = "0",
            salesChange = "0",
            salesAcc = "0",
            audiCnt = "0",
            audiInten = "0",
            audiChange = "0",
            audiAcc = "0",
            scrnCnt = "0",
            showCnt = "0"
        ),
        posterUrl = this.posterUrl
    )
} 