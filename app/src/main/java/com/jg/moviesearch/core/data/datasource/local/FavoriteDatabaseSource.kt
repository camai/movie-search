package com.jg.moviesearch.core.data.datasource.local

import kotlinx.coroutines.flow.Flow
import com.jg.moviesearch.core.database.entity.FavoriteMovieEntity

/**
 * 즐겨찾기 로컬 데이터베이스 DataSource 인터페이스
 * Repository와 실제 데이터베이스 사이의 추상화 계층
 */
interface FavoriteDatabaseSource {
    
    /**
     * 즐겨찾기 영화 추가
     */
    suspend fun insertFavoriteMovie(favoriteMovie: FavoriteMovieEntity)
    
    /**
     * 즐겨찾기 영화 삭제
     */
    suspend fun deleteFavoriteMovie(favoriteMovie: FavoriteMovieEntity)
    
    /**
     * 영화 코드로 즐겨찾기 삭제
     */
    suspend fun deleteFavoriteMovieByMovieCd(movieCd: String)
    
    /**
     * 모든 즐겨찾기 영화 조회 (최신순)
     */
    fun getAllFavoriteMovies(): Flow<List<FavoriteMovieEntity>>
    
    /**
     * 특정 영화가 즐겨찾기인지 확인
     */
    fun isFavoriteMovie(movieCd: String): Flow<Boolean>
    
    /**
     * 즐겨찾기 영화 개수 조회
     */
    suspend fun getFavoriteMovieCount(): Int
    
    /**
     * 특정 영화 정보 조회
     */
    suspend fun getFavoriteMovieByMovieCd(movieCd: String): FavoriteMovieEntity?
} 