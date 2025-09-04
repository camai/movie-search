package com.jg.moviesearch.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import com.jg.moviesearch.core.domain.repository.FavoriteMovieRepository
import javax.inject.Inject

/**
 * 즐겨찾기 상태 확인 UseCase
 */

interface GetFavoriteMovieStatusUseCase {
    operator fun invoke(movieCd: String): Flow<Boolean>
}

class GetFavoriteMovieStatusUseCaseImpl @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository
): GetFavoriteMovieStatusUseCase {

    override operator fun invoke(movieCd: String): Flow<Boolean> {
        return favoriteMovieRepository.isFavoriteMovie(movieCd)
    }
} 