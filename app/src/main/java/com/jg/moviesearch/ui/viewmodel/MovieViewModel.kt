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
    
    fun getDailyBoxOffice(date: String = getYesterday()) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            movieRepository.getDailyBoxOffice(date).collect { result ->
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
    
    fun getWeeklyBoxOffice(date: String = getLastWeek()) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            movieRepository.getWeeklyBoxOffice(date).collect { result ->
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
    
    fun searchMovies(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            movieRepository.searchMoviesByName(query).collect { result ->
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