package com.jg.moviesearch.ui.utils

import androidx.recyclerview.widget.DiffUtil
import com.jg.moviesearch.ui.model.MovieDetailPageItemUiState

class MoviePageDiffCallback : DiffUtil.ItemCallback<MovieDetailPageItemUiState>() {
    override fun areItemsTheSame(
        oldItem: MovieDetailPageItemUiState,
        newItem: MovieDetailPageItemUiState
    ): Boolean {
        return oldItem.movieCd == newItem.movieCd
    }

    override fun areContentsTheSame(
        oldItem: MovieDetailPageItemUiState,
        newItem: MovieDetailPageItemUiState
    ): Boolean {
        return oldItem == newItem
    }
}