package com.jg.moviesearch.ui.model

sealed interface MovieDetailAction {
    data class GetMovieDetail(val movieCd: String) : MovieDetailAction
    data class ObserveFavoriteStatus(val movieCd: String) : MovieDetailAction
    data class ToggleFavorite(val movieCd: String, val movieTitle: String, val posterUrl: String?) : MovieDetailAction
}