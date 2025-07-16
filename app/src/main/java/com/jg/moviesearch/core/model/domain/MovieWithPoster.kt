package com.jg.moviesearch.core.model.domain

/**
 * 포스터 정보가 포함된 영화 도메인 모델
 */
data class MovieWithPoster(
    val movie: Movie,
    val posterUrl: String? = null,
    val backdropUrl: String? = null
) 