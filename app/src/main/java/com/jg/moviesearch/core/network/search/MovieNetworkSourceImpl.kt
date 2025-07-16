package com.jg.moviesearch.core.network.search

import com.jg.moviesearch.BuildConfig
import com.jg.moviesearch.core.data.datasource.network.MovieNetworkSource
import com.jg.moviesearch.core.model.dto.MovieListResponseDto
import com.jg.moviesearch.core.model.dto.BoxOfficeResponseDto
import com.jg.moviesearch.core.model.dto.MovieDetailResponseDto
import com.jg.moviesearch.core.model.dto.TmdbMovieSearchResponseDto
import com.jg.moviesearch.core.network.api.KobisApi
import com.jg.moviesearch.core.network.api.TmdbApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

class MovieNetworkSourceImpl @Inject constructor(
    private val kobisApi: KobisApi,
    private val tmdbApi: TmdbApi
): MovieNetworkSource {

    companion object {
        private val KOBIS_API_KEY = BuildConfig.KOBIS_API_KEY
        private val TMDB_API_KEY = BuildConfig.TMDB_API_KEY
    }

    // 영화 검색 관련 - 순수한 API 호출만
    override suspend fun searchMovieList(movieName: String): Response<MovieListResponseDto> {
        return kobisApi.searchMovieList(
            key = KOBIS_API_KEY,
            movieNm = movieName,
            itemPerPage = 10
        )
    }

    // 영화 상세 정보 관련 - 순수한 API 호출만
    override suspend fun getMovieDetail(movieCd: String): Response<MovieDetailResponseDto> {
        return kobisApi.getMovieDetail(
            key = KOBIS_API_KEY,
            movieCd = movieCd
        )
    }

    // 영화 이미지 관련 - 순수한 API 호출만
    override suspend fun searchMoviePoster(movieTitle: String): Response<TmdbMovieSearchResponseDto> {
        return tmdbApi.searchMovie(
            apiKey = TMDB_API_KEY,
            query = movieTitle
        )
    }
}