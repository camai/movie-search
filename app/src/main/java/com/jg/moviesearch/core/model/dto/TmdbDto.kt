package com.jg.moviesearch.core.model.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// TMDB 영화 검색 API 응답 DTO
@JsonClass(generateAdapter = true)
data class TmdbMovieSearchResponseDto(
    @Json(name = "results")
    val results: List<TmdbMovieDto>
)

@JsonClass(generateAdapter = true)
data class TmdbMovieDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "original_title")
    val originalTitle: String,
    @Json(name = "poster_path")
    val posterPath: String? = null,
    @Json(name = "backdrop_path")
    val backdropPath: String? = null,
    @Json(name = "overview")
    val overview: String,
    @Json(name = "release_date")
    val releaseDate: String,
    @Json(name = "vote_average")
    val voteAverage: Double,
    @Json(name = "vote_count")
    val voteCount: Int
) 