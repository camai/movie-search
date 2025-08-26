package com.jg.moviesearch.ui.model

data class MovieData(
    val movieCds: List<String>,
    val movieTitles: List<String>,
    val posterUrls: List<String?>
) {
    companion object {
        val EMPTY = MovieData(
            movieCds = emptyList(),
            movieTitles = emptyList(),
            posterUrls = emptyList()
        )
    }
}
