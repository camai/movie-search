package com.jg.moviesearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jg.moviesearch.core.domain.repository.MovieRepository
import com.jg.moviesearch.core.model.Movie
import com.jg.moviesearch.core.model.MovieDetail
import com.jg.moviesearch.core.model.MovieWithPoster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()
    
    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail: StateFlow<MovieDetail?> = _movieDetail.asStateFlow()
    
    // 실시간 검색을 위한 검색어 StateFlow
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    init {
        setupRealTimeSearch()
    }

    fun getMovieDetail(movieCd: String) {
        viewModelScope.launch {
            movieRepository.getMovieDetail(movieCd).collect { result ->
                result.onSuccess { detail ->
                    _movieDetail.value = detail
                }.onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message
                    )
                }
            }
        }
    }
    
    // 실시간 검색 설정
    private fun setupRealTimeSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // 300ms 대기
                .distinctUntilChanged() // 동일한 검색어 중복 제거
                .filter { it.trim().length >= 2 } // 최소 2글자 이상
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        flowOf(Result.success(emptyList()))
                    } else {
                        movieRepository.searchMoviesByName(query.trim())
                    }
                }
                .collect { result ->
                    result.onSuccess { movies ->
                        _uiState.value = _uiState.value.copy(
                            movies = movies,
                            isLoading = false,
                            error = null
                        )
                    }.onFailure { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
                }
        }
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
    
    // 즉시 검색 (기존 방식 유지)
    fun searchMovies(query: String) {
        updateSearchQuery(query)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    private fun getYesterday(): String {
        return LocalDate.now().minusDays(1).toString().replace("-", "")
    }
    
    private fun getLastWeek(): String {
        return LocalDate.now().minusWeeks(1).toString().replace("-", "")
    }
}

data class MovieUiState(
    val movies: List<MovieWithPoster> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 