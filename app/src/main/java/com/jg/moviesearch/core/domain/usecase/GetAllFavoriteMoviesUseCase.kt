package com.jg.moviesearch.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import com.jg.moviesearch.core.domain.repository.FavoriteMovieRepository
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import javax.inject.Inject

/**
 * 모든 즐겨찾기 영화 조회 UseCase
 */
interface GetAllFavoriteMoviesUseCase {
    operator fun invoke(): Flow<List<MovieWithPoster>>
}
class GetAllFavoriteMoviesUseCaseImpl @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository
): GetAllFavoriteMoviesUseCase {
    
    /**
     * 모든 즐겨찾기 영화 목록 조회
     * @return 즐겨찾기 영화 목록 Flow
     */
    override operator fun invoke(): Flow<List<MovieWithPoster>> {
        return favoriteMovieRepository.getAllFavoriteMovies()
    }
} 