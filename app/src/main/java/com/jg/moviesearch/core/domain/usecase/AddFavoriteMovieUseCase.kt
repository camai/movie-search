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

    override suspend operator fun invoke(movie: MovieWithPoster) {
        favoriteMovieRepository.addFavoriteMovie(movie)
    }
} 