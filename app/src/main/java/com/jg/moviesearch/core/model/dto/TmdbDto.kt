package com.jg.moviesearch.core.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TMDB 영화 검색 API 응답 DTO
@Serializable
data class TmdbMovieSearchResponseDto(
    @SerialName("results")
    val results: List<TmdbMovieDto>
)

@Serializable
data class TmdbMovieDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("overview")
    val overview: String,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
) 