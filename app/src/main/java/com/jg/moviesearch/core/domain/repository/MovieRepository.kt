package com.jg.moviesearch.core.domain.repository

import com.jg.moviesearch.core.model.domain.Movie
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    // Movie Search APIs
    fun searchMoviesByName(movieName: String): Flow<List<Movie>>
    fun getMovieDetail(movieCd: String): Flow<MovieDetail>
    fun getMoviePoster(movieTitle: String): Flow<String>
    fun searchMoviesWithPoster(movieName: String, page: Int): Flow<List<MovieWithPoster>>

    // Favorite Movie APIs
    suspend fun addFavoriteMovie(movie: MovieWithPoster)
    suspend fun removeFavoriteMovie(movieCd: String)
    suspend fun toggleFavoriteMovie(movie: MovieWithPoster)
    fun getAllFavoriteMovies(): Flow<List<MovieWithPoster>>
    fun isFavoriteMovie(movieCd: String): Flow<Boolean>
    suspend fun getFavoriteMovieCount(): Int
}
