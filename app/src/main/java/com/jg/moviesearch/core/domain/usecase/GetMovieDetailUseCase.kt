package com.jg.moviesearch.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import com.jg.moviesearch.core.domain.repository.MovieRepository
import com.jg.moviesearch.core.model.MovieResult
import com.jg.moviesearch.core.model.domain.MovieDetail
import javax.inject.Inject

interface GetMovieDetailUseCase {
    operator fun invoke(movieCd: String): Flow<MovieResult<MovieDetail>>
}

class GetMovieDetailUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetMovieDetailUseCase {
    override operator fun invoke(movieCd: String): Flow<MovieResult<MovieDetail>> {
        return movieRepository.getMovieDetail(movieCd)
    }
}
