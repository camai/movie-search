package com.jg.moviesearch.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import com.jg.moviesearch.ui.viewmodel.MovieViewModel
import com.jg.moviesearch.ui.viewmodel.MovieDisplayType
import com.jg.moviesearch.ui.viewmodel.MovieUiState

@Composable
fun SearchMoviceRoute(
    modifier: Modifier = Modifier,
    onMovieClickWithList: (List<MovieWithPoster>, Int) -> Unit = { _, _ -> },
    viewModel: MovieViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMovieScreen(
    modifier: Modifier = Modifier,
    uiState: MovieUiState,
    searchQuery: String,
    onMovieClickWithList: (List<MovieWithPoster>, Int) -> Unit = { _, _ -> },
    updateSearchQuery: (String) -> Unit,
    shouldLoadMore: (Int) -> Boolean = { false },
    loadMoreMovies: () -> Unit = {},
    toggleFavorite: (MovieWithPoster) -> Unit = {},
    clearError: () -> Unit,
) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("영화 검색") }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // 실시간 검색 입력 필드
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newText ->
                    updateSearchQuery(newText)
                },
                label = { Text("영화 검색") },
                placeholder = { Text("영화 제목을 입력하세요") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 결과 영역
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val listState = rememberLazyListState()
                
                // 무한 스크롤 감지
                LaunchedEffect(listState) {
                    snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                        .collect { lastVisibleItemIndex ->
                            if (lastVisibleItemIndex != null && shouldLoadMore(lastVisibleItemIndex)) {
                                loadMoreMovies()
                            }
                        }
                }
                
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(uiState.processedMovies) { index, movieDisplayItem ->
                        when (movieDisplayItem.displayType) {
                            is MovieDisplayType.Poster -> {
                                PosterMovieCard(
                                    movieWithPoster = movieDisplayItem.movieWithPoster,
                                    isFavorite = uiState.favoriteMovies.contains(movieDisplayItem.movieWithPoster.movie.movieCd),
                                    onMovieClick = {
                                        // 상세 화면 호출 - 원본 movies 리스트에서 인덱스 찾기
                                        val originalIndex = uiState.movies.indexOf(movieDisplayItem.movieWithPoster)
                                        onMovieClickWithList(uiState.movies, originalIndex)
                                    },
                                    onFavoriteClick = {
                                        // 즐겨찾기 토글 호출
                                        toggleFavorite(movieDisplayItem.movieWithPoster)
                                    }
                                )
                            }
                            is MovieDisplayType.Text -> {
                                TextMovieCard(
                                    movieWithPoster = movieDisplayItem.movieWithPoster,
                                    isFavorite = uiState.favoriteMovies.contains(movieDisplayItem.movieWithPoster.movie.movieCd),
                                    onMovieClick = {
                                        // 상세 화면 호출 - 원본 movies 리스트에서 인덱스 찾기
                                        val originalIndex = uiState.movies.indexOf(movieDisplayItem.movieWithPoster)
                                        onMovieClickWithList(uiState.movies, originalIndex)
                                    },
                                    onFavoriteClick = {
                                        // 즐겨찾기 On/Off 호출
                                        toggleFavorite(movieDisplayItem.movieWithPoster)
                                    }
                                )
                            }
                        }
                    }
                    
                    // 로딩 더 보기 상태 표시
                    if (uiState.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    
                    // 마지막 아이템 다음에 Gray 패딩 영역 추가
                    if (uiState.processedMovies.isNotEmpty() && !uiState.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                                    .background(Color.Gray)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PosterMovieCard(
    movieWithPoster: MovieWithPoster,
    isFavorite: Boolean,
    onMovieClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val movie = movieWithPoster.movie

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        onClick = onMovieClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            // 영화 포스터 이미지
            AsyncImage(
                model = movieWithPoster.posterUrl,
                contentDescription = "${movie.movieNm} 포스터",
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp),
                placeholder = null,
                error = null
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 영화 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = movie.movieNm,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onFavoriteClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "즐겨찾기",
                            tint = if (isFavorite) Color.Yellow else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "개봉일: ${movie.openDt}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun TextMovieCard(
    movieWithPoster: MovieWithPoster,
    isFavorite: Boolean,
    onMovieClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val movie = movieWithPoster.movie

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        onClick = onMovieClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            // 영화 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = movie.movieNm,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onFavoriteClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "즐겨찾기",
                            tint = if (isFavorite) Color.Yellow else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "개봉일: ${movie.openDt}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}