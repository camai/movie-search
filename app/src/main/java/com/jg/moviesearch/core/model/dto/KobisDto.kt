package com.jg.moviesearch.core.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 박스오피스 API 응답 DTO
@Serializable
data class BoxOfficeResponseDto(
    @SerialName("boxOfficeResult")
    val boxOfficeResult: BoxOfficeResultDto
)

@Serializable
data class BoxOfficeResultDto(
    @SerialName("boxofficeType")
    val boxOfficeType: String,
    @SerialName("showRange")
    val showRange: String,
    @SerialName("dailyBoxOfficeList")
    val dailyBoxOfficeList: List<MovieDto>
)

@Serializable
data class MovieDto(
    @SerialName("rnum")
    val rank: String,
    @SerialName("rankInten")
    val rankInten: String,
    @SerialName("rankOldAndNew")
    val rankOldAndNew: String,
    @SerialName("movieCd")
    val movieCd: String,
    @SerialName("movieNm")
    val movieNm: String,
    @SerialName("openDt")
    val openDt: String,
    @SerialName("salesAmt")
    val salesAmt: String,
    @SerialName("salesShare")
    val salesShare: String,
    @SerialName("salesInten")
    val salesInten: String,
    @SerialName("salesChange")
    val salesChange: String,
    @SerialName("salesAcc")
    val salesAcc: String,
    @SerialName("audiCnt")
    val audiCnt: String,
    @SerialName("audiInten")
    val audiInten: String,
    @SerialName("audiChange")
    val audiChange: String,
    @SerialName("audiAcc")
    val audiAcc: String,
    @SerialName("scrnCnt")
    val scrnCnt: String,
    @SerialName("showCnt")
    val showCnt: String
)

// 영화 상세정보 API 응답 DTO
@Serializable
data class MovieDetailResponseDto(
    @SerialName("movieInfoResult")
    val movieInfoResult: MovieInfoResultDto
)

@Serializable
data class MovieInfoResultDto(
    @SerialName("movieInfo")
    val movieInfo: MovieDetailDto
)

@Serializable
data class MovieDetailDto(
    @SerialName("movieCd")
    val movieCd: String,
    @SerialName("movieNm")
    val movieNm: String,
    @SerialName("movieNmEn")
    val movieNmEn: String,
    @SerialName("prdtYear")
    val prdtYear: String,
    @SerialName("showTm")
    val showTm: String,
    @SerialName("openDt")
    val openDt: String,
    @SerialName("prdtStatNm")
    val prdtStatNm: String,
    @SerialName("typeNm")
    val typeNm: String,
    @SerialName("nations")
    val nations: List<NationDto>,
    @SerialName("genres")
    val genres: List<GenreDto>,
    @SerialName("directors")
    val directors: List<DirectorDto>,
    @SerialName("actors")
    val actors: List<ActorDto>
)

@Serializable
data class NationDto(
    @SerialName("nationNm")
    val nationNm: String
)

@Serializable
data class GenreDto(
    @SerialName("genreNm")
    val genreNm: String
)

@Serializable
data class DirectorDto(
    @SerialName("peopleNm")
    val peopleNm: String
)

@Serializable
data class ActorDto(
    @SerialName("peopleNm")
    val peopleNm: String,
    @SerialName("cast")
    val cast: String
)

// 영화 목록조회 API 응답 DTO
@Serializable
data class MovieListResponseDto(
    @SerialName("movieListResult")
    val movieListResult: MovieListResultDto
)

@Serializable
data class MovieListResultDto(
    @SerialName("totCnt")
    val totCnt: Int,
    @SerialName("source")
    val source: String,
    @SerialName("movieList")
    val movieList: List<MovieListItemDto>
)

@Serializable
data class MovieListItemDto(
    @SerialName("movieCd")
    val movieCd: String,
    @SerialName("movieNm")
    val movieNm: String,
    @SerialName("movieNmEn")
    val movieNmEn: String? = null,
    @SerialName("prdtYear")
    val prdtYear: String? = null,
    @SerialName("openDt")
    val openDt: String? = null,
    @SerialName("typeNm")
    val typeNm: String? = null,
    @SerialName("prdtStatNm")
    val prdtStatNm: String? = null,
    @SerialName("nationAlt")
    val nationAlt: String? = null,
    @SerialName("genreAlt")
    val genreAlt: String? = null,
    @SerialName("repNationNm")
    val repNationNm: String? = null,
    @SerialName("repGenreNm")
    val repGenreNm: String? = null,
    @SerialName("directors")
    val directors: List<MovieDirectorDto>? = null,
    @SerialName("companys")
    val companys: List<MovieCompanyDto>? = null
)

@Serializable
data class MovieDirectorDto(
    @SerialName("peopleNm")
    val peopleNm: String
)

@Serializable
data class MovieCompanyDto(
    @SerialName("companyCd")
    val companyCd: String,
    @SerialName("companyNm")
    val companyNm: String
) 