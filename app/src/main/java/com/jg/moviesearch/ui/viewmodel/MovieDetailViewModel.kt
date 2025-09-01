package com.jg.moviesearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jg.moviesearch.core.domain.usecase.AddFavoriteMovieUseCase
import com.jg.moviesearch.core.domain.usecase.GetFavoriteMovieStatusUseCase
import com.jg.moviesearch.core.domain.usecase.GetMovieDetailUseCase
import com.jg.moviesearch.core.domain.usecase.RemoveFavoriteMovieUseCase
import com.jg.moviesearch.core.model.MovieResult
import com.jg.moviesearch.core.model.domain.Movie
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import com.jg.moviesearch.ui.model.MovieData
import com.jg.moviesearch.ui.model.MovieDetailPageItemUiState
import com.jg.moviesearch.ui.model.MovieDetailUiEffect
import com.jg.moviesearch.ui.model.MovieDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val _uiEffect = Channel<MovieDetailUiEffect>()
    val uiEffect = _uiEffect.receiveAsFlow()

    // ==================== 영화 상세 정보 관련 ====================

    fun initializePages(movie: MovieData, initialPosition: Int) {
        val initialPages = movie.movieCds.mapIndexed { index, movieCd ->
            MovieDetailPageItemUiState(
                movieCd = movieCd,
                movieTitle = movie.movieTitles.getOrNull(index) ?: "",
                posterUrl = movie.posterUrls.getOrNull(index)
            )
        }
        _uiState.update { state ->
            state.copy(
                pages = initialPages,
                currentPosition = initialPosition
            )
        }

        loadPageDetails(initialPosition)
    }

    fun loadPageDetails(position: Int) {
        val page = _uiState.value.pages.getOrNull(position) ?: return
        if (page.movieDetail != null || page.isLoading) return

        viewModelScope.launch {
            updatePageState(position = position) {
                it.copy(isLoading = true)
            }

            getMovieDetailUseCase(movieCd = page.movieCd).collect { result ->
                val detail = if (result is MovieResult.Success) result.data else null
                updatePageState(position = position) {
                    it.copy(isLoading = false, movieDetail = detail)
                }
            }

            getFavoriteMovieStatusUseCase(movieCd = page.movieCd)
                .onEach { isFavorite ->
                    updatePageState(position = position) {
                        it.copy(isFavorite = isFavorite)
                    }
            }.launchIn(viewModelScope)

        }
    }

    // ==================== 즐겨찾기 관련 ====================

    // 즐겨찾기 토글
    fun toggleFavorite(position: Int) {
        val page = _uiState.value.pages.getOrNull(position) ?: return
        viewModelScope.launch {
            try {
                if (page.isFavorite) {
                    removeFavoriteMovieUseCase(movieCd = page.movieCd)
                } else {
                    addFavoriteMovieUseCase(
                        movie = MovieWithPoster(
                            movie = Movie.notFavoriteMovie(
                                movieCd = page.movieCd,
                                movieTitle = page.movieTitle
                            ),
                            posterUrl = page.posterUrl
                        )
                    )
                }
            } catch (e: Exception) {
                handleError(message = "즐겨찾기 처리 중 오류가 발생했습니다: ${e.message}")
            }
        }
    }

    // ==================== UI 상태 관리 ====================

    private fun handleError(message: String) {
        _uiEffect.trySend(MovieDetailUiEffect.ShowError(message = message))
    }

    private fun updatePageState(position: Int, updater: (MovieDetailPageItemUiState) -> MovieDetailPageItemUiState) {
        _uiState.update { state ->
            val newPages = state.pages.toMutableList()
            newPages[position] = updater(newPages[position])
            state.copy(pages = newPages)
        }
    }
}