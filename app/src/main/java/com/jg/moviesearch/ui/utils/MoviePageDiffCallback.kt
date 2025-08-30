package com.jg.moviesearch.ui.utils

import androidx.recyclerview.widget.DiffUtil
import com.jg.moviesearch.ui.model.MovieDetailPageItemUiState

class MoviePageDiffCallback(
    private val oldList: List<MovieDetailPageItemUiState>,
    private val newList: List<MovieDetailPageItemUiState>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].movieCd == newList[newItemPosition].movieCd
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.movieTitle == newItem.movieTitle &&
                oldItem.posterUrl == newItem.posterUrl &&
                oldItem.movieDetail == newItem.movieDetail
    }
}