package com.jg.moviesearch.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.jg.moviesearch.core.domain.usecase.GetMovieDetailUseCase
import com.jg.moviesearch.core.domain.usecase.AddFavoriteMovieUseCase
import com.jg.moviesearch.core.domain.usecase.RemoveFavoriteMovieUseCase
import com.jg.moviesearch.core.domain.usecase.GetFavoriteMovieStatusUseCase
import com.jg.moviesearch.core.model.MovieResult
import com.jg.moviesearch.core.model.domain.Movie
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
    private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase,
    private val getFavoriteMovieStatusUseCase: GetFavoriteMovieStatusUseCase
) : ViewModel() {
    
    private val _uiState = MutableLiveData(MovieDetailUiState())
    val uiState: LiveData<MovieDetailUiState> = _uiState
    
    // ==================== 영화 상세 정보 관련 ====================
    
    // 영화 상세 정보 조회
    fun getMovieDetail(movieCd: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
                
                getMovieDetailUseCase(movieCd).collect { result ->
                    when (result) {
                        is MovieResult.Loading -> {
                            _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
                        }
                        is MovieResult.Success -> {
                            _uiState.value = _uiState.value?.copy(
                                movieDetail = result.data,
                                isLoading = false,
                                error = null
                            )
                        }
                        is MovieResult.Error -> {
                            _uiState.value = _uiState.value?.copy(
                                isLoading = false,
                                error = "영화 상세 정보를 불러올 수 없습니다: ${result.message}"
                            )
                        }
                        is MovieResult.Empty -> {
                            _uiState.value = _uiState.value?.copy(
                                isLoading = false,
                                error = "영화 상세 정보가 없습니다"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    error = "오류가 발생했습니다: ${e.message}"
                )
            }
        }
    }
    
    // ==================== 즐겨찾기 관련 ====================
    
    // 즐겨찾기 상태 관찰
    fun observeFavoriteStatus(movieCd: String) {
        getFavoriteMovieStatusUseCase(movieCd = movieCd)
            .onEach { isFavorite ->
                _uiState.value = _uiState.value?.copy(isFavorite = isFavorite)
            }
            .launchIn(viewModelScope)
    }
    
    // 즐겨찾기 토글
    fun toggleFavorite(movieCd: String, movieTitle: String, posterUrl: String?) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value?.isFavorite ?: false
                
                if (currentState) {
                    removeFavoriteMovieUseCase(movieCd = movieCd)
                } else {
                    val movieWithPoster = MovieWithPoster(
                        movie = Movie.notFavoriteMovie(movieCd, movieTitle),
                        posterUrl = posterUrl
                    )
                    addFavoriteMovieUseCase(movie = movieWithPoster)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    error = "즐겨찾기 처리 중 오류가 발생했습니다: ${e.message}"
                )
            }
        }
    }
}

data class MovieDetailUiState(
    val movieDetail: MovieDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false
) 