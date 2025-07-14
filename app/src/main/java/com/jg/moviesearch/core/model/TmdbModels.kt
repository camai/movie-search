package com.jg.moviesearch.core.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TmdbMovieSearchResponse(
    @Json(name = "results")
    val results: List<TmdbMovie>
)

@JsonClass(generateAdapter = true)
data class TmdbMovie(
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

data class MovieWithPoster(
    val movie: Movie,
    val posterUrl: String? = null,
    val backdropUrl: String? = null
) 