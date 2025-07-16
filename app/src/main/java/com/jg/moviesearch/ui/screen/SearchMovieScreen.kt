package com.jg.moviesearch.ui.screen

import android.content.Context
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import com.jg.moviesearch.ui.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMovieScreen(
    modifier: Modifier = Modifier,
    onMovieClick: (String, String, String?) -> Unit = { _, _, _ -> },
    viewModel: MovieViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
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
                    viewModel.updateSearchQuery(newText)
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
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.movies) { movieWithPoster ->
                        MovieCard(
                            movieWithPoster = movieWithPoster,
                            onMovieClick = { 
                                // Fragment로 이동하는 콜백 호출
                                onMovieClick(
                                    movieWithPoster.movie.movieCd,
                                    movieWithPoster.movie.movieNm,
                                    movieWithPoster.posterUrl
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movieWithPoster: MovieWithPoster,
    onMovieClick: () -> Unit
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
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "즐겨찾기",
                        tint = Color.Yellow
                    )
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