package com.jg.moviesearch.core.domain.usecase

import com.jg.moviesearch.core.domain.repository.MovieRepository
import com.jg.moviesearch.core.model.domain.MovieDetailWithFavorite
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

/**
 * 영화 상세 정보와 즐겨찾기 상태를 함께 조회하는 UseCase
 */
interface GetMovieDetailWithFavoriteUseCase {
    suspend operator fun invoke(movieCd: String): MovieDetailWithFavorite
}

class GetMovieDetailWithFavoriteUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetMovieDetailWithFavoriteUseCase {
    
    override suspend operator fun invoke(movieCd: String): MovieDetailWithFavorite {
        return coroutineScope {
            val movieDetailDeferred = async { movieRepository.getMovieDetail(movieCd).firstOrNull() }
            val isFavoriteDeferred = async { movieRepository.isFavoriteMovie(movieCd).firstOrNull() }
            
            val movieDetail = movieDetailDeferred.await() 
                ?: throw Exception("영화 상세 정보를 조회할 수 없습니다")
            
            val isFavorite = try {
                isFavoriteDeferred.await() ?: false
            } catch (e: Exception) {
                false // 즐겨찾기 조회 실패 시 기본값
            }
            
            MovieDetailWithFavorite(movieDetail, isFavorite)
        }
    }
}