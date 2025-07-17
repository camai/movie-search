package com.jg.moviesearch.core.domain.usecase

import com.jg.moviesearch.core.domain.repository.FavoriteMovieRepository
import javax.inject.Inject

/**
 * 즐겨찾기 제거 UseCase
 */
interface RemoveFavoriteMovieUseCase {
    suspend operator fun invoke(movieCd: String)
}

class RemoveFavoriteMovieUseCaseImpl @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository
): RemoveFavoriteMovieUseCase {
    
    /**
     * 영화를 즐겨찾기에서 제거
     * @param movieCd 제거할 영화 코드
     */
    override suspend operator fun invoke(movieCd: String) {
        favoriteMovieRepository.removeFavoriteMovie(movieCd)
    }
} 