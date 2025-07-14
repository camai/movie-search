package com.jg.moviesearch.core.data.network

import com.jg.moviesearch.core.model.TmdbMovieSearchResponse
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
    ): Response<TmdbMovieSearchResponse>
    
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
        const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780"
    }
} 