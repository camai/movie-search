package com.jg.moviesearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jg.moviesearch.core.domain.usecase.AddFavoriteMovieUseCase
import com.jg.moviesearch.core.domain.usecase.GetFavoriteMovieStatusUseCase
import com.jg.moviesearch.core.domain.usecase.GetMovieDetailUseCase
import com.jg.moviesearch.core.domain.usecase.RemoveFavoriteMovieUseCase
import com.jg.moviesearch.core.model.MovieResult
import com.jg.moviesearch.core.model.domain.Movie
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import com.jg.moviesearch.ui.model.MovieDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
    private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase,
    private val getFavoriteMovieStatusUseCase: GetFavoriteMovieStatusUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState.EMPTY)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail: StateFlow<MovieDetail?> = _movieDetail.asStateFlow()

    // ==================== 영화 상세 정보 관련 ====================

    // 영화 상세 정보 조회
    fun getMovieDetail(movieCd: String) {
        viewModelScope.launch {
            try {
                showLoading()

                getMovieDetailUseCase(movieCd = movieCd).collect { result ->
                    when (result) {
                        is MovieResult.Loading -> {
                            showLoading()
                        }

                        is MovieResult.Success -> {
                            _movieDetail.update { result.data }
                            _uiState.update { state ->
                                state.copy(
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }

                        is MovieResult.Error -> {
                            handleError(message = "영화 상세 정보를 불러올 수 없습니다")
                        }

                        is MovieResult.Empty -> {
                            handleError(message = "영화 상세 정보가 없습니다")
                        }
                    }
                }
            } catch (e: Exception) {
                handleError(message = "영화 상세 정보를 불러올 수 없습니다: ${e.message}")
            }
        }
    }

    // ==================== 즐겨찾기 관련 ====================

    // 즐겨찾기 상태 관찰
    fun observeFavoriteStatus(movieCd: String) {
        getFavoriteMovieStatusUseCase(movieCd = movieCd)
            .onEach { isFavorite ->
                _uiState.update { state ->
                    state.copy(isFavorite = isFavorite)
                }
            }
            .launchIn(viewModelScope)
    }

    // 즐겨찾기 토글
    fun toggleFavorite(movieCd: String, movieTitle: String, posterUrl: String?) {
        viewModelScope.launch {
            try {
                if (_uiState.value.isFavorite) {
                    removeFavoriteMovieUseCase(movieCd = movieCd)
                } else {
                    addFavoriteMovieUseCase(
                        movie =
                            MovieWithPoster(
                                movie = Movie.notFavoriteMovie(movieCd, movieTitle),
                                posterUrl = posterUrl
                            )
                    )
                }
            } catch (e: Exception) {
                handleError(message = "즐겨찾기 처리 중 오류가 발생했습니다: ${e.message}")
            }
        }
    }

    // ==================== UI 상태 관리 ====================

    private fun showLoading() {
        _uiState.update { state ->
            state.copy(isLoading = true, error = null)
        }
    }

    private fun handleError(message: String) {
        _uiState.update { state ->
            state.copy(isLoading = false, error = message)
        }
    }
}