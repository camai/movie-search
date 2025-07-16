package com.jg.moviesearch.core.domain.repository

import kotlinx.coroutines.flow.Flow
import com.jg.moviesearch.core.model.MovieResult
import com.jg.moviesearch.core.model.domain.Movie
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.core.model.domain.MovieWithPoster

interface MovieRepository {
    // 영화 목록 조회 - Flow<MovieResult<List<Movie>>> 반환
    fun searchMoviesByName(movieName: String): Flow<MovieResult<List<Movie>>>
    
    // 박스오피스 검색 - Flow<MovieResult<List<Movie>>> 반환
    fun searchInBoxOffice(movieName: String): Flow<MovieResult<List<Movie>>>
    
    // 영화 상세 정보 조회 - Flow<MovieResult<MovieDetail>> 반환
    fun getMovieDetail(movieCd: String): Flow<MovieResult<MovieDetail>>
    
    // 영화 포스터 URL 조회 - Flow<MovieResult<String>> 반환
    fun getMoviePoster(movieTitle: String): Flow<MovieResult<String>>
    
    // 포스터와 함께 영화 검색 - Flow<MovieResult<List<MovieWithPoster>>> 반환
    fun searchMoviesWithPoster(movieName: String): Flow<MovieResult<List<MovieWithPoster>>>
} 