package com.jg.moviesearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jg.moviesearch.ui.fragment.MovieDetailFragment
import com.jg.moviesearch.ui.fragment.SearchMovieFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        if (savedInstanceState == null) {
            showSearchFragment()
        }
    }
    
    private fun showSearchFragment() {
        val fragment = SearchMovieFragment()
        
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    fun showMovieDetailFragment(movieCd: String, movieTitle: String, posterUrl: String?) {
        val fragment = MovieDetailFragment.newInstance(movieCd, movieTitle, posterUrl)
        
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("MovieDetail")
            .commit()
    }
}