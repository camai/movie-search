package com.jg.moviesearch.core.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// 영화목록조회 API 응답 모델들
@JsonClass(generateAdapter = true)
data class MovieListResponse(
    @Json(name = "movieListResult")
    val movieListResult: MovieListResult
)

@JsonClass(generateAdapter = true)
data class MovieListResult(
    @Json(name = "totCnt")
    val totCnt: Int,
    @Json(name = "source")
    val source: String,
    @Json(name = "movieList")
    val movieList: List<MovieListItem>
)

@JsonClass(generateAdapter = true)
data class MovieListItem(
    @Json(name = "movieCd")
    val movieCd: String,
    @Json(name = "movieNm")
    val movieNm: String,
    @Json(name = "movieNmEn")
    val movieNmEn: String?,
    @Json(name = "prdtYear")
    val prdtYear: String?,
    @Json(name = "openDt")
    val openDt: String?,
    @Json(name = "typeNm")
    val typeNm: String?,
    @Json(name = "prdtStatNm")
    val prdtStatNm: String?,
    @Json(name = "nationAlt")
    val nationAlt: String?,
    @Json(name = "genreAlt")
    val genreAlt: String?,
    @Json(name = "repNationNm")
    val repNationNm: String?,
    @Json(name = "repGenreNm")
    val repGenreNm: String?,
    @Json(name = "directors")
    val directors: List<MovieDirector>?,
    @Json(name = "companys")
    val companys: List<MovieCompany>?
)

@JsonClass(generateAdapter = true)
data class MovieDirector(
    @Json(name = "peopleNm")
    val peopleNm: String
)

@JsonClass(generateAdapter = true)
data class MovieCompany(
    @Json(name = "companyCd")
    val companyCd: String,
    @Json(name = "companyNm")
    val companyNm: String
) 