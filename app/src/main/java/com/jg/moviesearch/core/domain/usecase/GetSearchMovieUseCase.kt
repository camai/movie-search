package com.jg.moviesearch.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import com.jg.moviesearch.core.domain.repository.MovieRepository
import com.jg.moviesearch.core.model.MovieResult
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import javax.inject.Inject

interface GetSearchMovieUseCase {
    operator fun invoke(query: String, page: Int = 1): Flow<MovieResult<List<MovieWithPoster>>>
}

class GetSearchMovieUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetSearchMovieUseCase {
    
    override operator fun invoke(query: String, page: Int): Flow<MovieResult<List<MovieWithPoster>>> {
        // Repository에서 검색 결과 반환 (페이지 포함)
        return movieRepository.searchMoviesWithPoster(query, page)
    }
}