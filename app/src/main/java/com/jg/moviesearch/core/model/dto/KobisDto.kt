package com.jg.moviesearch.core.model.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

// 박스오피스 API 응답 DTO
@JsonClass(generateAdapter = true)
data class BoxOfficeResponseDto(
    @Json(name = "boxOfficeResult")
    val boxOfficeResult: BoxOfficeResultDto
)

@JsonClass(generateAdapter = true)
data class BoxOfficeResultDto(
    @Json(name = "boxofficeType")
    val boxOfficeType: String,
    @Json(name = "showRange")
    val showRange: String,
    @Json(name = "dailyBoxOfficeList")
    val dailyBoxOfficeList: List<MovieDto>
)

@JsonClass(generateAdapter = true)
data class MovieDto(
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

// 영화 상세정보 API 응답 DTO
@JsonClass(generateAdapter = true)
data class MovieDetailResponseDto(
    @Json(name = "movieInfoResult")
    val movieInfoResult: MovieInfoResultDto
)

@JsonClass(generateAdapter = true)
data class MovieInfoResultDto(
    @Json(name = "movieInfo")
    val movieInfo: MovieDetailDto
)

@JsonClass(generateAdapter = true)
data class MovieDetailDto(
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
    val nations: List<NationDto>,
    @Json(name = "genres")
    val genres: List<GenreDto>,
    @Json(name = "directors")
    val directors: List<DirectorDto>,
    @Json(name = "actors")
    val actors: List<ActorDto>
)

@JsonClass(generateAdapter = true)
data class NationDto(
    @Json(name = "nationNm")
    val nationNm: String
)

@JsonClass(generateAdapter = true)
data class GenreDto(
    @Json(name = "genreNm")
    val genreNm: String
)

@JsonClass(generateAdapter = true)
data class DirectorDto(
    @Json(name = "peopleNm")
    val peopleNm: String
)

@JsonClass(generateAdapter = true)
data class ActorDto(
    @Json(name = "peopleNm")
    val peopleNm: String,
    @Json(name = "cast")
    val cast: String
)

// 영화 목록조회 API 응답 DTO
@JsonClass(generateAdapter = true)
data class MovieListResponseDto(
    @Json(name = "movieListResult")
    val movieListResult: MovieListResultDto
)

@JsonClass(generateAdapter = true)
data class MovieListResultDto(
    @Json(name = "totCnt")
    val totCnt: Int,
    @Json(name = "source")
    val source: String,
    @Json(name = "movieList")
    val movieList: List<MovieListItemDto>
)

@JsonClass(generateAdapter = true)
data class MovieListItemDto(
    @Json(name = "movieCd")
    val movieCd: String,
    @Json(name = "movieNm")
    val movieNm: String,
    @Json(name = "movieNmEn")
    val movieNmEn: String? = null,
    @Json(name = "prdtYear")
    val prdtYear: String? = null,
    @Json(name = "openDt")
    val openDt: String? = null,
    @Json(name = "typeNm")
    val typeNm: String? = null,
    @Json(name = "prdtStatNm")
    val prdtStatNm: String? = null,
    @Json(name = "nationAlt")
    val nationAlt: String? = null,
    @Json(name = "genreAlt")
    val genreAlt: String? = null,
    @Json(name = "repNationNm")
    val repNationNm: String? = null,
    @Json(name = "repGenreNm")
    val repGenreNm: String? = null,
    @Json(name = "directors")
    val directors: List<MovieDirectorDto>? = null,
    @Json(name = "companys")
    val companys: List<MovieCompanyDto>? = null
)

@JsonClass(generateAdapter = true)
data class MovieDirectorDto(
    @Json(name = "peopleNm")
    val peopleNm: String
)

@JsonClass(generateAdapter = true)
data class MovieCompanyDto(
    @Json(name = "companyCd")
    val companyCd: String,
    @Json(name = "companyNm")
    val companyNm: String
) 