package com.jg.moviesearch.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import com.jg.moviesearch.core.data.datasource.network.MovieNetworkSource
import com.jg.moviesearch.core.domain.repository.MovieRepository
import com.jg.moviesearch.core.model.MovieResult
import com.jg.moviesearch.core.model.domain.Movie
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import com.jg.moviesearch.core.model.mapper.MovieMapper.toDomainModel
import com.jg.moviesearch.core.network.api.TmdbApi
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

class MovieRepositoryImpl @Inject constructor(
    private val movieNetworkSource: MovieNetworkSource
) : MovieRepository {
    
    override fun searchMoviesByName(movieName: String): Flow<MovieResult<List<Movie>>> = flow {
        emit(MovieResult.Loading)
        
        try {
            val response = movieNetworkSource.searchMovieList(movieName)
            
            if (response.isSuccessful) {
                val movieListResult = response.body()?.movieListResult
                val movieItems = movieListResult?.movieList
                
                if (movieItems != null && movieItems.isNotEmpty()) {
                    val domainMovies = ArrayList<Movie>()
                    for (item in movieItems) {
                        domainMovies.add(item.toDomainModel())
                    }
                    emit(MovieResult.Success(domainMovies))
                } else {
                    emit(MovieResult.Empty)
                }
            } else {
                emit(MovieResult.Error(Exception("API 호출 실패: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(MovieResult.Error(e))
        }
    }
    
    override fun getMovieDetail(movieCd: String): Flow<MovieResult<MovieDetail>> = flow {
        emit(MovieResult.Loading)
        
        try {
            val response = movieNetworkSource.getMovieDetail(movieCd)
            
            if (response.isSuccessful) {
                val movieDetailDto = response.body()?.movieInfoResult?.movieInfo
                if (movieDetailDto != null) {
                    emit(MovieResult.Success(movieDetailDto.toDomainModel()))
                } else {
                    emit(MovieResult.Error(Exception("영화 상세 정보가 없습니다")))
                }
            } else {
                emit(MovieResult.Error(Exception("영화 상세 정보 API 호출 실패: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(MovieResult.Error(e))
        }
    }
    
    override fun getMoviePoster(movieTitle: String): Flow<MovieResult<String>> = flow {
        emit(MovieResult.Loading)
        
        try {
            val response = movieNetworkSource.searchMoviePoster(movieTitle)
            
            if (response.isSuccessful) {
                val results = response.body()?.results
                if (results != null && results.isNotEmpty()) {
                    val posterPath = results.get(0).posterPath
                    if (posterPath != null && posterPath.isNotEmpty()) {
                        emit(MovieResult.Success("${TmdbApi.IMAGE_BASE_URL}$posterPath"))
                    } else {
                        emit(MovieResult.Error(Exception("포스터 이미지가 없습니다")))
                    }
                } else {
                    emit(MovieResult.Error(Exception("포스터 검색 결과가 없습니다")))
                }
            } else {
                emit(MovieResult.Error(Exception("포스터 검색 API 호출 실패: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(MovieResult.Error(e))
        }
    }
    
    override fun searchMoviesWithPoster(movieName: String, page: Int): Flow<MovieResult<List<MovieWithPoster>>> = flow {
        emit(MovieResult.Loading)
        
        try {
            // 1. 영화 검색
            val movieResponse = movieNetworkSource.searchMovieList(movieName, page)
            
            if (movieResponse.isSuccessful) {
                val movieListResult = movieResponse.body()?.movieListResult
                val movieItems = movieListResult?.movieList
                
                if (movieItems != null && movieItems.isNotEmpty()) {
                    val moviesWithPoster = ArrayList<MovieWithPoster>()
                    
                    // 2. 각 영화에 대해 포스터 URL 추가
                    for (item in movieItems) {
                        val movie = item.toDomainModel()
                        
                        // 포스터 조회 (실패해도 계속 진행)
                        val posterUrl = try {
                            val posterResponse = movieNetworkSource.searchMoviePoster(movie.movieNm)
                            if (posterResponse.isSuccessful) {
                                val results = posterResponse.body()?.results
                                if (results != null && results.isNotEmpty()) {
                                    val posterPath = results.get(0).posterPath
                                    if (posterPath != null && posterPath.isNotEmpty()) {
                                        "${TmdbApi.IMAGE_BASE_URL}$posterPath"
                                    } else null
                                } else null
                            } else null
                        } catch (e: Exception) {
                            null
                        }
                        
                        moviesWithPoster.add(
                            MovieWithPoster(
                                movie = movie,
                                posterUrl = posterUrl
                            )
                        )
                    }
                    
                    // 3. 개봉일 기준 내림차순 정렬 (최신 순)
                    val sortedMovies = sortMoviesByOpenDate(moviesWithPoster)
                    emit(MovieResult.Success(sortedMovies))
                } else {
                    emit(MovieResult.Empty)
                }
            } else {
                emit(MovieResult.Error(Exception("영화 검색 API 호출 실패: ${movieResponse.code()}")))
            }
        } catch (e: Exception) {
            emit(MovieResult.Error(e))
        }
    }
    
    private fun sortMoviesByOpenDate(movies: List<MovieWithPoster>): List<MovieWithPoster> {
        val sortedMovies = ArrayList(movies)
        // 개봉일 기준 내림차순 정렬 (최신 순)
        sortedMovies.sortWith { a, b ->
            val dateA = a.movie.openDt
            val dateB = b.movie.openDt
            dateB.compareTo(dateA)
        }
        return sortedMovies
    }
} 