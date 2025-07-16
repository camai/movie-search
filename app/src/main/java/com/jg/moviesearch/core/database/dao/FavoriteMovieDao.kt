package com.jg.moviesearch.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.jg.moviesearch.core.database.entity.FavoriteMovieEntity

/**
 * 즐겨찾기 영화 DAO
 * Room 데이터베이스의 즐겨찾기 영화 테이블에 대한 데이터 접근 객체
 */
@Dao
interface FavoriteMovieDao {
    
    /**
     * 즐겨찾기 영화 추가
     * @param favoriteMovie 추가할 즐겨찾기 영화 정보
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteMovie(favoriteMovie: FavoriteMovieEntity)
    
    /**
     * 즐겨찾기 영화 삭제
     * @param favoriteMovie 삭제할 즐겨찾기 영화 정보
     */
    @Delete
    suspend fun deleteFavoriteMovie(favoriteMovie: FavoriteMovieEntity)
    
    /**
     * 영화 코드로 즐겨찾기 삭제
     * @param movieCd 삭제할 영화 코드
     */
    @Query("DELETE FROM favorite_movies WHERE movieCd = :movieCd")
    suspend fun deleteFavoriteMovieByMovieCd(movieCd: String)
    
    /**
     * 모든 즐겨찾기 영화 조회 (최신순)
     * @return 즐겨찾기 영화 목록 Flow
     */
    @Query("SELECT * FROM favorite_movies ORDER BY createdAt DESC")
    fun getAllFavoriteMovies(): Flow<List<FavoriteMovieEntity>>
    
    /**
     * 특정 영화가 즐겨찾기인지 확인
     * @param movieCd 확인할 영화 코드
     * @return 즐겨찾기 여부 Flow
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE movieCd = :movieCd)")
    fun isFavoriteMovie(movieCd: String): Flow<Boolean>
    
    /**
     * 즐겨찾기 영화 개수 조회
     * @return 즐겨찾기 영화 개수
     */
    @Query("SELECT COUNT(*) FROM favorite_movies")
    suspend fun getFavoriteMovieCount(): Int
    
    /**
     * 특정 영화 정보 조회
     * @param movieCd 조회할 영화 코드
     * @return 즐겨찾기 영화 정보
     */
    @Query("SELECT * FROM favorite_movies WHERE movieCd = :movieCd")
    suspend fun getFavoriteMovieByMovieCd(movieCd: String): FavoriteMovieEntity?
} 