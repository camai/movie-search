package com.jg.moviesearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jg.moviesearch.ui.screen.SearchMovieScreen
import com.jg.moviesearch.ui.theme.MovieSearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieSearchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SearchMovieScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}