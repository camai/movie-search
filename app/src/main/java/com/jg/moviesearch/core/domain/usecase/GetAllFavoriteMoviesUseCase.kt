package com.jg.moviesearch.core.domain.usecase

import com.jg.moviesearch.core.domain.repository.MovieRepository
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 모든 즐겨찾기 영화 조회 UseCase
 */
interface GetAllFavoriteMoviesUseCase {
    operator fun invoke(): Flow<List<MovieWithPoster>>
}

class GetAllFavoriteMoviesUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetAllFavoriteMoviesUseCase {

    override operator fun invoke(): Flow<List<MovieWithPoster>> {
        return movieRepository.getAllFavoriteMovies()
    }
} 