package com.jg.moviesearch.ui.screen

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jg.moviesearch.core.model.Movie
import com.jg.moviesearch.core.model.MovieWithPoster
import com.jg.moviesearch.ui.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMovieScreen(
    modifier: Modifier = Modifier,
    viewModel: MovieViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var searchQuery by remember { mutableStateOf("") }
    
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
                title = { Text("한국 영화 검색") }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // 검색 영역
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("영화 제목 검색") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.searchMovies(searchQuery) },
                    modifier = Modifier.weight(1f),
                    enabled = searchQuery.isNotBlank()
                ) {
                    Text("검색")
                }
                
                Button(
                    onClick = { viewModel.getDailyBoxOffice() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("일일 박스오피스")
                }
                
                Button(
                    onClick = { viewModel.getWeeklyBoxOffice() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("주간 박스오피스")
                }
            }
            
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
                            onMovieClick = { viewModel.getMovieDetail(movieWithPoster.movie.movieCd) }
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
                    Text(
                        text = "${movie.rank}위",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "개봉일: ${movie.openDt}",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Text(
                    text = "관객수: ${movie.audiCnt}명",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                if (movie.rankInten.isNotBlank()) {
                    Text(
                        text = "순위 변화: ${movie.rankInten}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}