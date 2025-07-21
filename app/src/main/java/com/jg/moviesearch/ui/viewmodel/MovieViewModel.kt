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
    
    // 현재 검색 쿼리 저장 (페이지네이션용)
    private var currentSearchQuery: String = ""
    
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
    
    // 영화 데이터를 표시 타입과 함께 처리하는 함수
    private fun processMoviesForDisplay(movies: List<MovieWithPoster>): List<MovieDisplayItem> {
        return movies.mapIndexed { index, movieWithPoster ->
            MovieDisplayItem(movieWithPoster, MovieDisplayType.fromIndex(index))
        }
    }
    
    private fun searchMoviesInternal(query: String) {
        // 새로운 검색 시 페이지 상태 초기화
        currentSearchQuery = query
        _uiState.value = _uiState.value.copy(
            currentPage = 1,
            hasMoreData = true,
            movies = emptyList(),
            processedMovies = emptyList() // 초기화
        )
        
        getSearchMovieUseCase(query, 1)
            .onEach { result ->
                when (result) {
                    is MovieResult.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    is MovieResult.Success -> {
                        val processedMovies = processMoviesForDisplay(result.data)
                        _uiState.value = _uiState.value.copy(
                            movies = result.data,
                            processedMovies = processedMovies,
                            isLoading = false,
                            error = null,
                            hasMoreData = result.data.size >= 10 // 10개 이상이면 더 있을 수 있음
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
                            processedMovies = emptyList(),
                            isLoading = false,
                            error = "검색 결과가 없습니다",
                            hasMoreData = false
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
    
    // 추가 데이터 로드 (무한 스크롤)
    fun loadMoreMovies() {
        val currentState = _uiState.value
        
        // 이미 로딩 중이거나 더 이상 데이터가 없으면 중단
        if (currentState.isLoadingMore || !currentState.hasMoreData || currentSearchQuery.isEmpty()) {
            return
        }
        
        _uiState.value = currentState.copy(isLoadingMore = true)
        
        // 다음 페이지 데이터 로드
        getSearchMovieUseCase(currentSearchQuery, currentState.currentPage + 1)
            .onEach { result ->
                when (result) {
                    is MovieResult.Success -> {
                        val currentMovies = _uiState.value.movies
                        val newMovies = result.data // 새로운 페이지 데이터
                        val allMovies = currentMovies + newMovies
                        val processedMovies = processMoviesForDisplay(allMovies)
                        
                        _uiState.value = _uiState.value.copy(
                            movies = allMovies,
                            processedMovies = processedMovies,
                            isLoadingMore = false,
                            currentPage = currentState.currentPage + 1,
                            hasMoreData = newMovies.isNotEmpty() && newMovies.size >= 10 // 10개 이상이면 더 있을 수 있음
                        )
                    }
                    is MovieResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoadingMore = false,
                            error = "추가 데이터 로드 실패: ${result.message}"
                        )
                    }
                    is MovieResult.Empty -> {
                        _uiState.value = _uiState.value.copy(
                            isLoadingMore = false,
                            hasMoreData = false
                        )
                    }
                    is MovieResult.Loading -> {
                        // 이미 isLoadingMore로 로딩 상태 표시 중
                    }
                }
            }
            .launchIn(viewModelScope)
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
    
    // 즐겨찾기 on/off 기능
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
    val processedMovies: List<MovieDisplayItem> = emptyList(), // 추가
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val favoriteMovies: Set<String> = emptySet(),
    val hasMoreData: Boolean = true,
    val currentPage: Int = 1
)

// 새로운 데이터 클래스 추가
data class MovieDisplayItem(
    val movieWithPoster: MovieWithPoster,
    val displayType: MovieDisplayType
)

// sealed class로 표시 타입 정의
sealed class MovieDisplayType {
    object Poster : MovieDisplayType()
    object Text : MovieDisplayType()
    
    companion object {
        fun fromIndex(index: Int): MovieDisplayType {
            return if ((index % 6) < 3) Poster else Text
        }
    }
} 