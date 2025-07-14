package com.jg.moviesearch.core.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BoxOfficeResponse(
    @Json(name = "boxOfficeResult")
    val boxOfficeResult: BoxOfficeResult
)

@JsonClass(generateAdapter = true)
data class BoxOfficeResult(
    @Json(name = "boxofficeType")
    val boxOfficeType: String,
    @Json(name = "showRange")
    val showRange: String,
    @Json(name = "dailyBoxOfficeList")
    val dailyBoxOfficeList: List<Movie>
)

@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "rnum")
    val rank: String,
    @Json(name = "rankInten")
    val rankInten: String,
    @Json(name = "rankOldAndNew")
    val rankOldAndNew: String,
    @Json(name = "movieCd")
    val movieCd: String,
    @Json(name = "movieNm")
    val movieNm: String,
    @Json(name = "openDt")
    val openDt: String,
    @Json(name = "salesAmt")
    val salesAmt: String,
    @Json(name = "salesShare")
    val salesShare: String,
    @Json(name = "salesInten")
    val salesInten: String,
    @Json(name = "salesChange")
    val salesChange: String,
    @Json(name = "salesAcc")
    val salesAcc: String,
    @Json(name = "audiCnt")
    val audiCnt: String,
    @Json(name = "audiInten")
    val audiInten: String,
    @Json(name = "audiChange")
    val audiChange: String,
    @Json(name = "audiAcc")
    val audiAcc: String,
    @Json(name = "scrnCnt")
    val scrnCnt: String,
    @Json(name = "showCnt")
    val showCnt: String
)

@JsonClass(generateAdapter = true)
data class MovieDetailResponse(
    @Json(name = "movieInfoResult")
    val movieInfoResult: MovieInfoResult
)

@JsonClass(generateAdapter = true)
data class MovieInfoResult(
    @Json(name = "movieInfo")
    val movieInfo: MovieDetail
)

@JsonClass(generateAdapter = true)
data class MovieDetail(
    @Json(name = "movieCd")
    val movieCd: String,
    @Json(name = "movieNm")
    val movieNm: String,
    @Json(name = "movieNmEn")
    val movieNmEn: String,
    @Json(name = "prdtYear")
    val prdtYear: String,
    @Json(name = "showTm")
    val showTm: String,
    @Json(name = "openDt")
    val openDt: String,
    @Json(name = "prdtStatNm")
    val prdtStatNm: String,
    @Json(name = "typeNm")
    val typeNm: String,
    @Json(name = "nations")
    val nations: List<Nation>,
    @Json(name = "genres")
    val genres: List<Genre>,
    @Json(name = "directors")
    val directors: List<Director>,
    @Json(name = "actors")
    val actors: List<Actor>
)

@JsonClass(generateAdapter = true)
data class Nation(
    @Json(name = "nationNm")
    val nationNm: String
)

@JsonClass(generateAdapter = true)
data class Genre(
    @Json(name = "genreNm")
    val genreNm: String
)

@JsonClass(generateAdapter = true)
data class Director(
    @Json(name = "peopleNm")
    val peopleNm: String
)

@JsonClass(generateAdapter = true)
data class Actor(
    @Json(name = "peopleNm")
    val peopleNm: String,
    @Json(name = "cast")
    val cast: String
)

 