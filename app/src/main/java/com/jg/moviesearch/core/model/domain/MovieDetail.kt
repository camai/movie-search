package com.jg.moviesearch.core.model.domain

/**
 * 영화 상세정보 도메인 모델
 */
data class MovieDetail(
    val movieCd: String,
    val movieNm: String,
    val movieNmEn: String,
    val prdtYear: String,
    val showTm: String,
    val openDt: String,
    val prdtStatNm: String,
    val typeNm: String,
    val nations: List<Nation>,
    val genres: List<Genre>,
    val directors: List<Director>,
    val actors: List<Actor>
)

data class Nation(
    val nationNm: String
)

data class Genre(
    val genreNm: String
)

data class Director(
    val peopleNm: String
)

data class Actor(
    val peopleNm: String,
    val cast: String
) 