package com.jg.moviesearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import com.jg.moviesearch.ui.activity.MovieDetailActivity
import com.jg.moviesearch.ui.screen.SearchMovieScreen
import com.jg.moviesearch.ui.theme.MovieSearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val composeView = ComposeView(this).apply {
            setContent {
                MovieSearchTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        SearchMovieScreen(
                            modifier = Modifier.padding(innerPadding),
                            onMovieClickWithList = { movieList, currentIndex ->
                                startMovieDetailActivity(movieList, currentIndex)
                            }
                        )
                    }
                }
            }
        }
        
        setContentView(composeView)
    }

    private fun startMovieDetailActivity(movieList: List<MovieWithPoster>, currentIndex: Int) {
        val movieCds = movieList.map { it.movie.movieCd }
        val movieTitles = movieList.map { it.movie.movieNm }
        val posterUrls = movieList.map { it.posterUrl }

        val intent = MovieDetailActivity.newIntent(
            context = this,
            movieCds = movieCds,
            movieTitles = movieTitles,
            posterUrls = posterUrls,
            currentPosition = currentIndex
        )
        
        startActivity(intent)
    }
}