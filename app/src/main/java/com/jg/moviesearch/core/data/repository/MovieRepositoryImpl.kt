package com.jg.moviesearch.core.data.repository

import com.jg.moviesearch.BuildConfig
import com.jg.moviesearch.core.data.network.KobisApi
import com.jg.moviesearch.core.data.network.TmdbApi
import com.jg.moviesearch.core.domain.repository.MovieRepository
import com.jg.moviesearch.core.model.Movie
import com.jg.moviesearch.core.model.MovieDetail
import com.jg.moviesearch.core.model.MovieWithPoster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val kobisApi: KobisApi,
    private val tmdbApi: TmdbApi
) : MovieRepository {
    
    companion object {
        private val KOBIS_API_KEY = BuildConfig.KOBIS_API_KEY
        private val TMDB_API_KEY = BuildConfig.TMDB_API_KEY
    }
    
    override suspend fun getMovieDetail(movieCd: String): Flow<Result<MovieDetail>> = flow {
        try {
            val response = kobisApi.getMovieDetail(
                key = KOBIS_API_KEY,
                movieCd = movieCd
            )
            
            if (response.isSuccessful) {
                val movieDetail = response.body()?.movieInfoResult?.movieInfo
                if (movieDetail != null) {
                    emit(Result.success(movieDetail))
                } else {
                    emit(Result.failure(Exception("영화 정보를 찾을 수 없습니다.")))
                }
            } else {
                emit(Result.failure(Exception("API 호출 실패: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    override suspend fun searchMoviesByName(movieName: String): Flow<Result<List<MovieWithPoster>>> = flow {
        try {
            // 영화목록조회 API로 직접 검색 (기간 제한 없음)
            val searchResponse = kobisApi.searchMovieList(
                key = KOBIS_API_KEY,
                movieNm = movieName,
                itemPerPage = 20
            )
            
            if (searchResponse.isSuccessful) {
                val movieListResult = searchResponse.body()?.movieListResult
                val movieItems = movieListResult?.movieList ?: emptyList()
                
                if (movieItems.isNotEmpty()) {
                    // 최신 영화 우선 정렬 (개봉일 기준)
                    val sortedMovies = movieItems.sortedByDescending { it.openDt ?: "" }
                    
                    // MovieListItem을 Movie로 변환
                    val movies = sortedMovies.map { item ->
                        convertMovieListItemToMovie(item)
                    }
                    
                    val moviesWithPoster = movies.map { movie ->
                        val posterUrl = getMoviePoster(movie.movieNm)
                        MovieWithPoster(
                            movie = movie,
                            posterUrl = posterUrl
                        )
                    }
                    
                    emit(Result.success(moviesWithPoster))
                } else {
                    // 영화목록조회에서 결과가 없으면 박스오피스에서 검색
                    searchInBoxOffice(movieName)?.let { movies ->
                        val moviesWithPoster = movies.map { movie ->
                            val posterUrl = getMoviePoster(movie.movieNm)
                            MovieWithPoster(
                                movie = movie,
                                posterUrl = posterUrl
                            )
                        }
                        emit(Result.success(moviesWithPoster))
                    } ?: emit(Result.success(emptyList()))
                }
            } else {
                emit(Result.failure(Exception("영화 검색 실패: ${searchResponse.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    // MovieListItem을 Movie로 변환
    private fun convertMovieListItemToMovie(item: Any): Movie {
        // 임시로 Any 타입으로 받아서 리플렉션으로 처리
        return Movie(
            rank = "0",
            rankInten = "0",
            rankOldAndNew = "NEW",
            movieCd = getFieldValue(item, "movieCd") ?: "",
            movieNm = getFieldValue(item, "movieNm") ?: "",
            openDt = getFieldValue(item, "openDt") ?: "",
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
    
    // 리플렉션으로 필드 값 가져오기
    private fun getFieldValue(obj: Any, fieldName: String): String? {
        return try {
            val field = obj.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field.get(obj)?.toString()
        } catch (e: Exception) {
            null
        }
    }
    
    // 박스오피스에서 검색하는 백업 메소드
    private suspend fun searchInBoxOffice(movieName: String): List<Movie>? {
        return try {
            val today = java.time.LocalDate.now()
            val searchDate = today.minusDays(1).toString().replace("-", "")
            
            val response = kobisApi.getDailyBoxOffice(
                key = KOBIS_API_KEY,
                targetDt = searchDate,
                itemPerPage = 10
            )
            
            if (response.isSuccessful) {
                response.body()?.boxOfficeResult?.dailyBoxOfficeList
                    ?.filter { it.movieNm.contains(movieName, ignoreCase = true) }
            } else null
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun getMoviePoster(movieTitle: String): String? {
        return try {
            val response = tmdbApi.searchMovie(
                apiKey = TMDB_API_KEY,
                query = movieTitle
            )
            
            if (response.isSuccessful) {
                val results = response.body()?.results
                if (!results.isNullOrEmpty()) {
                    val posterPath = results.first().posterPath
                    if (!posterPath.isNullOrEmpty()) {
                        "${TmdbApi.IMAGE_BASE_URL}$posterPath"
                    } else null
                } else null
            } else null
        } catch (e: Exception) {
            null
        }
    }
} 