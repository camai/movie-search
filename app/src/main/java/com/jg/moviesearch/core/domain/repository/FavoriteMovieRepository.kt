package com.jg.moviesearch.core.domain.repository

import kotlinx.coroutines.flow.Flow
import com.jg.moviesearch.core.model.domain.MovieWithPoster

/**
 * 즐겨찾기 영화 Repository 인터페이스
 * 즐겨찾기 관련 비즈니스 로직을 정의하는 도메인 계층 인터페이스
 */
interface FavoriteMovieRepository {
    
    /**
     * 즐겨찾기 영화 추가
     * @param movie 추가할 영화 정보
     */
    suspend fun addFavoriteMovie(movie: MovieWithPoster)
    
    /**
     * 즐겨찾기 영화 삭제
     * @param movieCd 삭제할 영화 코드
     */
    suspend fun removeFavoriteMovie(movieCd: String)
    
    /**
     * 즐겨찾기 상태 토글
     * @param movie 토글할 영화 정보
     */
    suspend fun toggleFavoriteMovie(movie: MovieWithPoster)
    
    /**
     * 모든 즐겨찾기 영화 조회
     * @return 즐겨찾기 영화 목록 Flow
     */
    fun getAllFavoriteMovies(): Flow<List<MovieWithPoster>>
    
    /**
     * 특정 영화가 즐겨찾기인지 확인
     * @param movieCd 확인할 영화 코드
     * @return 즐겨찾기 여부 Flow
     */
    fun isFavoriteMovie(movieCd: String): Flow<Boolean>
    
    /**
     * 즐겨찾기 영화 개수 조회
     * @return 즐겨찾기 영화 개수
     */
    suspend fun getFavoriteMovieCount(): Int
} 