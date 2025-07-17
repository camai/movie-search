package com.jg.moviesearch.core.domain.usecase

import com.jg.moviesearch.core.domain.repository.FavoriteMovieRepository
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import javax.inject.Inject

/**
 * 즐겨찾기 추가 UseCase
 */
interface AddFavoriteMovieUseCase {
    suspend operator fun invoke(movie: MovieWithPoster)
}
class AddFavoriteMovieUseCaseImpl @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository
): AddFavoriteMovieUseCase {
    
    /**
     * 영화를 즐겨찾기에 추가
     * @param movie 추가할 영화 정보
     */
    override suspend operator fun invoke(movie: MovieWithPoster) {
        favoriteMovieRepository.addFavoriteMovie(movie)
    }
} 