package com.jg.moviesearch.core.domain.usecase

import com.jg.moviesearch.core.domain.repository.MovieRepository
import com.jg.moviesearch.core.model.domain.MovieDetailWithFavorite
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 영화 상세 정보와 즐겨찾기 상태를 함께 조회하는 UseCase
 * 두 개의 Repository 호출을 병렬로 처리하여 성능 최적화
 */
interface GetMovieDetailWithFavoriteUseCase {
    suspend operator fun invoke(movieCd: String): Flow<MovieDetailWithFavorite>
}

class GetMovieDetailWithFavoriteUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetMovieDetailWithFavoriteUseCase {
    
    override suspend operator fun invoke(movieCd: String): Flow<MovieDetailWithFavorite> = flow {
        coroutineScope {
            // 병렬로 두 데이터 동시 조회
            val movieDetailDeferred = async { movieRepository.getMovieDetail(movieCd).first() }
            val isFavoriteDeferred = async { movieRepository.isFavoriteMovie(movieCd).first() }
            
            val movieDetail = movieDetailDeferred.await()
            val isFavorite = isFavoriteDeferred.await()
            
            emit(MovieDetailWithFavorite(movieDetail, isFavorite))
        }
    }
}