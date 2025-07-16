package com.jg.moviesearch.core.data.datasource.network

import com.jg.moviesearch.core.model.dto.MovieListResponseDto
import com.jg.moviesearch.core.model.dto.BoxOfficeResponseDto
import com.jg.moviesearch.core.model.dto.MovieDetailResponseDto
import com.jg.moviesearch.core.model.dto.TmdbMovieSearchResponseDto
import retrofit2.Response

interface MovieNetworkSource {
    // 영화 검색
    suspend fun searchMovieList(movieName: String, page: Int = 1): Response<MovieListResponseDto>

    // 영화 상세 정보
    suspend fun getMovieDetail(movieCd: String): Response<MovieDetailResponseDto>
    
    // 영화 이미지
    suspend fun searchMoviePoster(movieTitle: String): Response<TmdbMovieSearchResponseDto>
}