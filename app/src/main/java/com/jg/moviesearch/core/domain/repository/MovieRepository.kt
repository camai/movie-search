package com.jg.moviesearch.core.domain.repository

import com.jg.moviesearch.core.model.MovieDetail
import com.jg.moviesearch.core.model.MovieWithPoster
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getMovieDetail(movieCd: String): Flow<Result<MovieDetail>>

    suspend fun searchMoviesByName(movieName: String): Flow<Result<List<MovieWithPoster>>>

    suspend fun getMoviePoster(movieTitle: String): String?
} 