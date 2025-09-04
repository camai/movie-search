package com.jg.moviesearch.core.model.domain

/**
 * 영화 상세 정보와 즐겨찾기 상태를 함께 담는 도메인 모델
 */
data class MovieDetailWithFavorite(
    val movieDetail: MovieDetail,
    val isFavorite: Boolean
)