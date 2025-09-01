package com.jg.moviesearch.ui.model

import com.jg.moviesearch.core.model.domain.MovieWithPoster

sealed interface SearchMovieAction {
    data class UpdateSearchQuery(val query: String) : SearchMovieAction
    data class ToggleFavorite(val movie: MovieWithPoster) : SearchMovieAction
    data class MovieClick(val movie: MovieWithPoster, val movieList: List<MovieWithPoster>) : SearchMovieAction
}