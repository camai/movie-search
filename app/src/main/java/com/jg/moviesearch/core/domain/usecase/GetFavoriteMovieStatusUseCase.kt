package com.jg.moviesearch.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import com.jg.moviesearch.core.domain.repository.FavoriteMovieRepository
import javax.inject.Inject

/**
 * 즐겨찾기 상태 확인 UseCase
 * 특정 영화가 즐겨찾기인지 여부를 확인하는 비즈니스 로직을 처리
 */

interface GetFavoriteMovieStatusUseCase {
    operator fun invoke(movieCd: String): Flow<Boolean>
}

class GetFavoriteMovieStatusUseCaseImpl @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository
): GetFavoriteMovieStatusUseCase {
    
    /**
     * 특정 영화의 즐겨찾기 상태 확인
     * @param movieCd 확인할 영화 코드
     * @return 즐겨찾기 여부 Flow
     */
    override operator fun invoke(movieCd: String): Flow<Boolean> {
        return favoriteMovieRepository.isFavoriteMovie(movieCd)
    }
} 