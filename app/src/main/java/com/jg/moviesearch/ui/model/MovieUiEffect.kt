package com.jg.moviesearch.ui.model

import com.jg.moviesearch.core.model.domain.MovieWithPoster

sealed class MovieUiEffect {
    data class ShowError(val message: String) : MovieUiEffect()
    data class NavigateToDetail(
        val movieList: List<MovieWithPoster>,
        val position: Int,
    ) : MovieUiEffect()
}