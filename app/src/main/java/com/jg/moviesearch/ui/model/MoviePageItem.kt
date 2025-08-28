package com.jg.moviesearch.ui.model

import com.jg.moviesearch.core.model.domain.MovieDetail

data class MoviePageItem(
    val movieCd: String,
    val movieTitle: String,
    val posterUrl: String?,
    val movieDetail: MovieDetail = MovieDetail.EMPTY
) {
    companion object {
        val EMPTY = MoviePageItem(
            movieCd = "",
            movieTitle = "",
            posterUrl = null
        )
    }
}