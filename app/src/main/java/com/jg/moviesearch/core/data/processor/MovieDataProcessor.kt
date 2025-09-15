package com.jg.moviesearch.core.data.processor

import com.jg.moviesearch.core.model.domain.Movie
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import com.jg.moviesearch.core.model.dto.TmdbMovieSearchResponseDto
import com.jg.moviesearch.core.network.config.NetworkConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 영화 데이터 처리기
 * Android Layer Architecture - 복잡한 데이터 조합 로직 분리
 */
@Singleton  
class MovieDataProcessor @Inject constructor(
    private val networkConfig: NetworkConfig
) {
    
    /**
     * 영화 데이터와 포스터 URL을 결합
     * @param movies 영화 데이터 리스트
     * @param posterResponses 포스터 응답 맵 (영화명 -> TMDB 응답)
     * @return 포스터가 포함된 영화 데이터 리스트
     */
    fun combineMoviesWithPosters(
        movies: List<Movie>,
        posterResponses: Map<String, TmdbMovieSearchResponseDto?>
    ): List<MovieWithPoster> {
        return movies.map { movie ->
            val posterUrl = extractPosterUrl(posterResponses[movie.movieNm])
            MovieWithPoster(
                movie = movie,
                posterUrl = posterUrl
            )
        }
    }
    
    /**
     * 개봉일 기준 내림차순 정렬 (최신 순)
     * @param moviesWithPoster 정렬할 영화 리스트
     * @return 정렬된 영화 리스트
     */
    fun sortByReleaseDate(moviesWithPoster: List<MovieWithPoster>): List<MovieWithPoster> {
        return moviesWithPoster.sortedWith { a, b ->
            val dateA = a.movie.openDt
            val dateB = b.movie.openDt
            dateB.compareTo(dateA)
        }
    }
    
    /**
     * TMDB 응답에서 포스터 URL 추출
     * @param tmdbResponse TMDB API 응답
     * @return 포스터 URL 또는 null
     */
    private fun extractPosterUrl(tmdbResponse: TmdbMovieSearchResponseDto?): String? {
        val results = tmdbResponse?.results
        if (results.isNullOrEmpty()) return null
        
        val posterPath = results.firstOrNull()?.posterPath
        return if (!posterPath.isNullOrEmpty()) {
            "${networkConfig.tmdbImageBaseUrl}$posterPath"
        } else {
            null
        }
    }
}