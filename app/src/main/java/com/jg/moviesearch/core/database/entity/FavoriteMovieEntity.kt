package com.jg.moviesearch.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 즐겨찾기 영화 Entity
 * Room 데이터베이스에서 즐겨찾기 영화 정보를 저장하는 테이블
 */
@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey
    val movieCd: String,
    val movieNm: String,
    val openDt: String,
    val posterUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) 