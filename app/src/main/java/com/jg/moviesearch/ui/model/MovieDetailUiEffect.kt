package com.jg.moviesearch.ui.model

sealed class MovieDetailUiEffect {
    data class ShowError(val message: String) : MovieDetailUiEffect()
}