package com.jg.moviesearch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jg.moviesearch.core.domain.usecase.AddFavoriteMovieUseCase
import com.jg.moviesearch.core.domain.usecase.GetAllFavoriteMoviesUseCase
import com.jg.moviesearch.core.domain.usecase.GetSearchMovieUseCase
import com.jg.moviesearch.core.domain.usecase.RemoveFavoriteMovieUseCase
import com.jg.moviesearch.core.model.MovieResult
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    // 실시간 검색을 위한 검색어
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // 현재 검색 쿼리
    private var currentSearchQuery: String = ""

    // ==================== 초기화 ====================
    init {
        setupRealTimeSearch()
        observeFavoriteMovies()
    }

    // ==================== 검색 관련 함수들 ====================

    // 실시간 검색 설정
    private fun setupRealTimeSearch() {
        _searchQuery
            .debounce(300)
            .filter { it.trim().length >= 2 }
            .onEach { query ->
                searchMoviesInternal(query = query.trim())
            }
            .launchIn(viewModelScope)
    }

    // 검색어 업데이트
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.trim().length >= 2) {
            showLoading()
        } else if (query.trim().isEmpty()) {
            clearSearchResults()
        }
    }

    // 내부 검색
    private fun searchMoviesInternal(query: String) {
        currentSearchQuery = query
        resetSearchState()

        getSearchMovieUseCase(query, 1)
            .onEach { result ->
                when (result) {
                    is MovieResult.Loading -> showLoading()
                    is MovieResult.Success -> handleSearchSuccess(movies = result.data)
                    is MovieResult.Error -> handleError(
                        message = result.message,
                        prefix = "영화 검색 실패"
                    )

                    is MovieResult.Empty -> handleSearchEmpty()
                }
            }
            .launchIn(viewModelScope)
    }

    // ==================== 무한 스크롤 관련 함수들 ====================

    // 무한 스크롤 조건 체크
    fun shouldLoadMore(lastVisibleItemIndex: Int): Boolean {
        val currentState = _uiState.value
        return lastVisibleItemIndex >= currentState.movies.size - 3 &&
                currentState.hasMoreData &&
                !currentState.isLoadingMore &&
                currentState.movies.size >= 10
    }

    // 추가 데이터 로드 (무한 스크롤)
    fun loadMoreMovies() {
        val currentState = _uiState.value

        if (currentState.isLoadingMore || !currentState.hasMoreData || currentSearchQuery.isEmpty()) {
            return
        }

        _uiState.update { state ->
            state.copy(isLoadingMore = true)
        }

        getSearchMovieUseCase(currentSearchQuery, currentState.currentPage + 1)
            .onEach { result ->
                when (result) {
                    is MovieResult.Success -> handleLoadMoreSuccess(
                        newMovies = result.data,
                        currentPage = currentState.currentPage
                    )

                    is MovieResult.Error -> handleError(
                        message = result.message,
                        prefix = "추가 데이터 로드 실패",
                        isLoadMore = true
                    )

                    is MovieResult.Empty -> handleLoadMoreEmpty()
                    is MovieResult.Loading -> { /* 이미 isLoadingMore로 처리 중 */
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    // ==================== 즐겨찾기 관련 함수들 ====================

    // 즐겨찾기 상태 관찰
    private fun observeFavoriteMovies() {
        getAllFavoriteMoviesUseCase()
            .onEach { favoriteMovies ->
                val favoriteMovieCds = favoriteMovies.map { it.movie.movieCd }.toSet()
                _uiState.update { state ->
                    state.copy(favoriteMovies = favoriteMovieCds)
                }
            }
            .launchIn(viewModelScope)
    }

    // 즐겨찾기
    fun toggleFavorite(movieWithPoster: MovieWithPoster) {
        viewModelScope.launch {
            try {
                val isFavorite =
                    _uiState.value.favoriteMovies.contains(movieWithPoster.movie.movieCd)

                if (isFavorite) {
                    removeFavoriteMovieUseCase(movieCd = movieWithPoster.movie.movieCd)
                } else {
                    addFavoriteMovieUseCase(movie = movieWithPoster)
                }
            } catch (e: Exception) {
                handleError(message = e.message ?: "Unknown error", prefix = "즐겨찾기 처리 중 오류")
            }
        }
    }

    // ==================== UI 상태 관리 ====================

    // 에러 메시지 표시
    fun clearError() {
        _uiState.update { state ->
            state.copy(error = null)
        }
    }

    // 로딩 상태 표시
    private fun showLoading() {
        _uiState.update { state ->
            state.copy(isLoading = true, error = null)
        }
    }

    // 공통 에러 처리 함수
    private fun handleError(message: String, prefix: String, isLoadMore: Boolean = false) {
        _uiState.update { state ->
            state.copy(
                isLoading = if (!isLoadMore) false else state.isLoading,
                isLoadingMore = if (isLoadMore) false else state.isLoadingMore,
                error = "$prefix: $message"
            )
        }
    }

    // 검색 결과 초기화
    private fun clearSearchResults() {
        _uiState.update {
            it.copy(
                movies = emptyList(),
                processedMovies = emptyList(),
                isLoading = false,
                error = null
            )
        }
    }

    // 검색 상태 초기화
    private fun resetSearchState() {
        _uiState.update {
            it.copy(
                currentPage = 1,
                hasMoreData = true,
                movies = emptyList(),
                processedMovies = emptyList()
            )
        }
    }

    // ==================== 검색 결과 처리 함수들 ====================

    // 검색 성공 처리
    private fun handleSearchSuccess(movies: List<MovieWithPoster>) {
        val processedMovies = processMoviesForDisplay(movies = movies)
        _uiState.update { state ->
            state.copy(
                movies = movies,
                processedMovies = processedMovies,
                isLoading = false,
                error = null,
                hasMoreData = movies.size >= 10
            )
        }
    }

    // 검색 결과 없음 처리
    private fun handleSearchEmpty() {
        _uiState.update { state ->
            state.copy(
                movies = emptyList(),
                processedMovies = emptyList(),
                isLoading = false,
                error = "검색 결과가 없습니다",
                hasMoreData = false
            )
        }
    }

    // ==================== 무한 스크롤 결과 처리 함수들 ====================

    // 무한 스크롤 성공 처리
    private fun handleLoadMoreSuccess(newMovies: List<MovieWithPoster>, currentPage: Int) {
        val currentMovies = _uiState.value.movies
        val allMovies = currentMovies + newMovies
        val processedMovies = processMoviesForDisplay(movies = allMovies)

        _uiState.update { state ->
            state.copy(
                movies = allMovies,
                processedMovies = processedMovies,
                isLoadingMore = false,
                currentPage = currentPage + 1,
                hasMoreData = newMovies.isNotEmpty() && newMovies.size >= 10
            )
        }
    }

    // 무한 스크롤 결과 없음 처리
    private fun handleLoadMoreEmpty() {
        _uiState.update { state ->
            state.copy(
                isLoadingMore = false,
                hasMoreData = false
            )
        }
    }

    // ==================== 유틸리티 함수들 ====================

    // 영화 데이터를 MovieDisplayItem으로 변환
    private fun processMoviesForDisplay(movies: List<MovieWithPoster>): List<MovieDisplayItem> {
        return movies.mapIndexed { index, movieWithPoster ->
            MovieDisplayItem(
                movieWithPoster = movieWithPoster,
                displayType = MovieDisplayType.fromIndex(index)
            )
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

data class MovieDisplayItem(
    val movieWithPoster: MovieWithPoster,
    val displayType: MovieDisplayType
)

sealed class MovieDisplayType {
    object Poster : MovieDisplayType()
    object Text : MovieDisplayType()

    companion object {
        fun fromIndex(index: Int): MovieDisplayType {
            return if ((index % 6) < 3) Poster else Text
        }
    }
} 