package com.jg.moviesearch.ui.model

import com.jg.moviesearch.core.model.domain.MovieDetail

data class MovieDetailPageItemUiState(
    val movieCd: String,
    val movieTitle: String,
    val posterUrl: String?,
    val movieDetail: MovieDetail = MovieDetail.EMPTY
) {
    companion object {
        val EMPTY = MovieDetailPageItemUiState(
            movieCd = "",
            movieTitle = "",
            posterUrl = null
        )
    }
}