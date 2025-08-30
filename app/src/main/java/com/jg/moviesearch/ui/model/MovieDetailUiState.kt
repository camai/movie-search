package com.jg.moviesearch.ui.model

import com.jg.moviesearch.core.model.domain.MovieDetail


data class MovieDetailUiState(
    val isLoading: Boolean,
    val error: String? = null,
    val movieDetailItem: MovieDetail,
    val isFavorite: Boolean
) {
    companion object {
        val EMPTY = MovieDetailUiState(
            isLoading = false,
            error = null,
            movieDetailItem = MovieDetail.EMPTY,
            isFavorite = false
        )
    }
}