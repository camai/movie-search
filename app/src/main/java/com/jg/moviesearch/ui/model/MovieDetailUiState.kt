package com.jg.moviesearch.ui.model


data class MovieDetailUiState(
    val isLoading: Boolean,
    val error: String? = null,
    val isFavorite: Boolean
) {
    companion object {
        val EMPTY = MovieDetailUiState(
            isLoading = false,
            error = null,
            isFavorite = false
        )
    }
}