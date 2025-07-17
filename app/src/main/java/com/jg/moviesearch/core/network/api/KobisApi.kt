package com.jg.moviesearch.core.network.api

import com.jg.moviesearch.core.model.dto.BoxOfficeResponseDto
import com.jg.moviesearch.core.model.dto.MovieDetailResponseDto
import com.jg.moviesearch.core.model.dto.MovieListResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KobisApi {
    
    @GET("boxoffice/searchDailyBoxOfficeList.json")
    suspend fun getDailyBoxOffice(
        @Query("key") key: String,
        @Query("targetDt") targetDt: String,
        @Query("itemPerPage") itemPerPage: Int = 10,
        @Query("multiMovieYn") multiMovieYn: String = "N",
        @Query("repNationCd") repNationCd: String = "K"
    ): Response<BoxOfficeResponseDto>
    
    @GET("movie/searchMovieInfo.json")
    suspend fun getMovieDetail(
        @Query("key") key: String,
        @Query("movieCd") movieCd: String
    ): Response<MovieDetailResponseDto>
    
    @GET("boxoffice/searchWeeklyBoxOfficeList.json")
    suspend fun getWeeklyBoxOffice(
        @Query("key") key: String,
        @Query("targetDt") targetDt: String,
        @Query("weekGb") weekGb: String = "0",
        @Query("itemPerPage") itemPerPage: Int = 10,
        @Query("multiMovieYn") multiMovieYn: String = "N",
        @Query("repNationCd") repNationCd: String = "K"
    ): Response<BoxOfficeResponseDto>
    
    @GET("movie/searchMovieList.json")
    suspend fun searchMovieList(
        @Query("key") key: String,
        @Query("movieNm") movieNm: String,
        @Query("itemPerPage") itemPerPage: Int = 50,
        @Query("curPage") curPage: Int = 1,
        @Query("openStartDt") openStartDt: String = "1980",
        @Query("openEndDt") openEndDt: String = ""
    ): Response<MovieListResponseDto>
    
    companion object {
        const val BASE_URL = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/"
    }
} 