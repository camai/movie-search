package com.jg.moviesearch.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import com.jg.moviesearch.core.domain.repository.MovieRepository
import com.jg.moviesearch.core.model.MovieResult
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import javax.inject.Inject

interface GetSearchMovieUseCase {
    operator fun invoke(query: String): Flow<MovieResult<List<MovieWithPoster>>>
}

class GetSearchMovieUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetSearchMovieUseCase {
    
    override operator fun invoke(query: String): Flow<MovieResult<List<MovieWithPoster>>> {
        // 단순하게 Repository에서 검색 결과만 반환
        return movieRepository.searchMoviesWithPoster(query)
    }
}