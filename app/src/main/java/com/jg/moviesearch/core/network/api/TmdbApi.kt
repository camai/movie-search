package com.jg.moviesearch.core.network.api

import com.jg.moviesearch.core.model.dto.TmdbMovieSearchResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    
    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "ko-KR",
        @Query("page") page: Int = 1
    ): Response<TmdbMovieSearchResponseDto>
    
} 