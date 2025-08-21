package com.jg.moviesearch.core.model.domain

/**
 * 영화 도메인 모델
 * UI 계층에서 사용되는 비즈니스 로직 모델
 */
data class Movie(
    val rank: String,
    val rankInten: String,
    val rankOldAndNew: String,
    val movieCd: String,
    val movieNm: String,
    val openDt: String,
    val salesAmt: String,
    val salesShare: String,
    val salesInten: String,
    val salesChange: String,
    val salesAcc: String,
    val audiCnt: String,
    val audiInten: String,
    val audiChange: String,
    val audiAcc: String,
    val scrnCnt: String,
    val showCnt: String
) {
    companion object {
        fun notFavoriteMovie(movieCd: String, movieTitle: String) = Movie(
            rank = "0",
            rankInten = "0",
            rankOldAndNew = "NEW",
            movieCd = movieCd,
            movieNm = movieTitle,
            openDt = "",
            salesAmt = "0",
            salesShare = "0.0",
            salesInten = "0",
            salesChange = "0",
            salesAcc = "0",
            audiCnt = "0",
            audiInten = "0",
            audiChange = "0",
            audiAcc = "0",
            scrnCnt = "0",
            showCnt = "0"
        )
    }
}