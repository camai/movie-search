package com.jg.moviesearch.ui.model


data class MovieDetailUiState(
    val pages: List<MovieDetailPageItemUiState>,
    val currentPosition: Int
) {
    companion object {
        val EMPTY = MovieDetailUiState(
            pages = emptyList(),
            currentPosition = 0
        )
    }
}