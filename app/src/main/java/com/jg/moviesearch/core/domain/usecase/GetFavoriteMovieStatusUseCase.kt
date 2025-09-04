package com.jg.moviesearch.core.domain.usecase

import com.jg.moviesearch.core.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 즐겨찾기 상태 확인 UseCase
 */

interface GetFavoriteMovieStatusUseCase {
    operator fun invoke(movieCd: String): Flow<Boolean>
}

class GetFavoriteMovieStatusUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetFavoriteMovieStatusUseCase {

    override operator fun invoke(movieCd: String): Flow<Boolean> {
        return movieRepository.isFavoriteMovie(movieCd)
    }
} 