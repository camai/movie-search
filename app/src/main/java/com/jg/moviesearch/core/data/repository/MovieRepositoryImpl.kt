package com.jg.moviesearch.core.data.repository

import com.jg.moviesearch.core.data.datasource.local.FavoriteDatabaseSource
import com.jg.moviesearch.core.data.datasource.network.MovieNetworkSource
import com.jg.moviesearch.core.data.processor.MovieDataProcessor
import com.jg.moviesearch.core.database.entity.FavoriteMovieEntity
import com.jg.moviesearch.core.domain.repository.MovieRepository
import com.jg.moviesearch.core.model.domain.Movie
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import com.jg.moviesearch.core.model.mapper.MovieMapper.toDomainModel
import com.jg.moviesearch.core.network.api.TmdbApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieNetworkSource: MovieNetworkSource,
    private val favoriteDatabaseSource: FavoriteDatabaseSource,
    private val movieDataProcessor: MovieDataProcessor
) : MovieRepository {

    // Movie Search APIs
    override fun searchMoviesByName(movieName: String): Flow<List<Movie>> = flow {
        val response = movieNetworkSource.searchMovieList(movieName)
        if (response.isSuccessful) {
            val movieItems = response.body()?.movieListResult?.movieList ?: emptyList()
            val domainMovies = movieItems.map { it.toDomainModel() }
            emit(domainMovies)
        } else {
            throw Exception("API 호출 실패: ${response.code()}")
        }
    }

    override fun getMovieDetail(movieCd: String): Flow<MovieDetail> = flow {
        val response = movieNetworkSource.getMovieDetail(movieCd)
        if (response.isSuccessful) {
            val movieDetailDto = response.body()?.movieInfoResult?.movieInfo
            if (movieDetailDto != null) {
                emit(movieDetailDto.toDomainModel())
            } else {
                throw Exception("영화 상세 정보가 없습니다")
            }
        } else {
            throw Exception("영화 상세 정보 API 호출 실패: ${response.code()}")
        }
    }

    override fun getMoviePoster(movieTitle: String): Flow<String> = flow {
        val response = movieNetworkSource.searchMoviePoster(movieTitle)
        if (response.isSuccessful) {
            val results = response.body()?.results
            if (!results.isNullOrEmpty()) {
                val posterPath = results[0].posterPath
                if (!posterPath.isNullOrEmpty()) {
                    emit("${TmdbApi.IMAGE_BASE_URL}$posterPath")
                } else {
                    throw Exception("포스터 이미지가 없습니다")
                }
            } else {
                throw Exception("포스터 검색 결과가 없습니다")
            }
        } else {
            throw Exception("포스터 검색 API 호출 실패: ${response.code()}")
        }
    }

    override fun searchMoviesWithPoster(movieName: String, page: Int): Flow<List<MovieWithPoster>> = flow {
        val movieResponse = movieNetworkSource.searchMovieList(movieName, page)
        if (movieResponse.isSuccessful) {
            val movieItems = movieResponse.body()?.movieListResult?.movieList ?: emptyList()
            
            if (movieItems.isEmpty()) {
                emit(emptyList())
                return@flow
            }

            val moviesWithPoster = coroutineScope {
                movieItems.map { item ->
                    async {
                        val movie = item.toDomainModel()
                        val posterUrl = try {
                            val posterResponse = movieNetworkSource.searchMoviePoster(movie.movieNm)
                            if (posterResponse.isSuccessful) {
                                val results = posterResponse.body()?.results
                                results?.firstOrNull()?.posterPath?.let { "${TmdbApi.IMAGE_BASE_URL}$it" }
                            } else {
                                null
                            }
                        } catch (e: Exception) {
                            null // 포스터 조회 실패 시 null 처리
                        }
                        MovieWithPoster(movie = movie, posterUrl = posterUrl)
                    }
                }.awaitAll()
            }

            val sortedMovies = sortMoviesByOpenDate(movies = moviesWithPoster)
            emit(sortedMovies)
        } else {
            throw Exception("영화 검색 API 호출 실패: ${movieResponse.code()}")
        }
    }

    private fun sortMoviesByOpenDate(movies: List<MovieWithPoster>): List<MovieWithPoster> {
        return movies.sortedByDescending { it.movie.openDt }
    }

    // Favorite Movie APIs
    override suspend fun addFavoriteMovie(movie: MovieWithPoster) {
        val favoriteEntity = FavoriteMovieEntity(
            movieCd = movie.movie.movieCd,
            movieNm = movie.movie.movieNm,
            openDt = movie.movie.openDt,
            posterUrl = movie.posterUrl,
            createdAt = System.currentTimeMillis()
        )
        favoriteDatabaseSource.insertFavoriteMovie(favoriteEntity)
    }

    override suspend fun removeFavoriteMovie(movieCd: String) {
        favoriteDatabaseSource.deleteFavoriteMovieByMovieCd(movieCd)
    }

    override suspend fun toggleFavoriteMovie(movie: MovieWithPoster) {
        val existingFavorite = favoriteDatabaseSource.getFavoriteMovieByMovieCd(movie.movie.movieCd)
        if (existingFavorite != null) {
            removeFavoriteMovie(movie.movie.movieCd)
        } else {
            addFavoriteMovie(movie)
        }
    }

    override fun getAllFavoriteMovies(): Flow<List<MovieWithPoster>> {
        return favoriteDatabaseSource.getAllFavoriteMovies().map { entities ->
            entities.map { entity ->
                entity.toMovieWithPoster()
            }
        }
    }

    override fun isFavoriteMovie(movieCd: String): Flow<Boolean> {
        return favoriteDatabaseSource.isFavoriteMovie(movieCd)
    }

    override suspend fun getFavoriteMovieCount(): Int {
        return favoriteDatabaseSource.getFavoriteMovieCount()
    }
}

private fun FavoriteMovieEntity.toMovieWithPoster(): MovieWithPoster {
    return MovieWithPoster(
        movie = Movie.fromFavorite(
            movieCd = this.movieCd,
            movieNm = this.movieNm,
            openDt = this.openDt
        ),
        posterUrl = this.posterUrl
    )
}
