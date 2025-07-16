package com.jg.moviesearch.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.jg.moviesearch.MainActivity
import com.jg.moviesearch.ui.screen.SearchMovieScreen
import com.jg.moviesearch.ui.theme.MovieSearchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMovieFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MovieSearchTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        SearchMovieScreen(
                            modifier = Modifier.padding(innerPadding),
                            onMovieClick = { movieCd, movieTitle, posterUrl ->
                                // MainActivity의 메서드 호출
                                (activity as? MainActivity)?.showMovieDetailFragment(
                                    movieCd, movieTitle, posterUrl
                                )
                            }
                        )
                    }
                }
            }
        }
    }
} 