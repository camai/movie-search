package com.jg.moviesearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.jg.moviesearch.core.domain.usecase.GetSearchMovieUseCase
import com.jg.moviesearch.core.domain.usecase.GetMovieDetailUseCase
import com.jg.moviesearch.core.domain.usecase.GetAllFavoriteMoviesUseCase
import com.jg.moviesearch.core.domain.usecase.AddFavoriteMovieUseCase
import com.jg.moviesearch.core.domain.usecase.RemoveFavoriteMovieUseCase
import com.jg.moviesearch.core.model.MovieResult
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getSearchMovieUseCase: GetSearchMovieUseCase,
    private val getAllFavoriteMoviesUseCase: GetAllFavoriteMoviesUseCase,
    private val addFavoriteMovieUseCase: AddFavoriteMovieUseCase,
    private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    // 실시간 검색을 위한 검색어 StateFlow
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    init {
        setupRealTimeSearch()
        observeFavoriteMovies()
    }
    
    // 실시간 검색 설정
    private fun setupRealTimeSearch() {
        _searchQuery
            .debounce(300) // 300ms 대기
            .distinctUntilChanged() // 동일한 검색어 중복 제거
            .filter { it.trim().length >= 2 } // 최소 2글자 이상
            .onEach { query ->
                if (query.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        movies = emptyList(),
                        isLoading = false,
                        error = null
                    )
                } else {
                    searchMoviesInternal(query.trim())
                }
            }
            .launchIn(viewModelScope)
    }
    
    private fun searchMoviesInternal(query: String) {
        getSearchMovieUseCase(query)
            .onEach { result ->
                when (result) {
                    is MovieResult.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    is MovieResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            movies = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is MovieResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "영화 검색 실패: ${result.message}"
                        )
                    }
                    is MovieResult.Empty -> {
                        _uiState.value = _uiState.value.copy(
                            movies = emptyList(),
                            isLoading = false,
                            error = "검색 결과가 없습니다"
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }
    
    // 검색어 업데이트 (UI에서 호출)
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.trim().length >= 2) {
            _uiState.value = _uiState.value.copy(isLoading = true)
        } else if (query.trim().isEmpty()) {
            _uiState.value = _uiState.value.copy(
                movies = emptyList(),
                isLoading = false,
                error = null
            )
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    // 즐겨찾기 상태 관찰
    private fun observeFavoriteMovies() {
        getAllFavoriteMoviesUseCase()
            .onEach { favoriteMovies ->
                val favoriteMovieCds = favoriteMovies.map { it.movie.movieCd }.toSet()
                _uiState.value = _uiState.value.copy(favoriteMovies = favoriteMovieCds)
            }
            .launchIn(viewModelScope)
    }
    
    // 즐겨찾기 토글 기능
    fun toggleFavorite(movieWithPoster: MovieWithPoster) {
        viewModelScope.launch {
            try {
                val isFavorite = _uiState.value.favoriteMovies.contains(movieWithPoster.movie.movieCd)
                
                if (isFavorite) {
                    // 즐겨찾기 상태라면 제거
                    removeFavoriteMovieUseCase(movieWithPoster.movie.movieCd)
                } else {
                    // 즐겨찾기가 아니라면 추가
                    addFavoriteMovieUseCase(movieWithPoster)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "즐겨찾기 처리 중 오류가 발생했습니다: ${e.message}")
            }
        }
    }
}

data class MovieUiState(
    val movies: List<MovieWithPoster> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val favoriteMovies: Set<String> = emptySet()
) 