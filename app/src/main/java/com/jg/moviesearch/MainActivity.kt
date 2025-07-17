package com.jg.moviesearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jg.moviesearch.core.model.domain.MovieWithPoster
import com.jg.moviesearch.ui.fragment.MovieDetailPagerFragment
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

    fun showMovieDetailPagerFragment(movieList: List<MovieWithPoster>, currentIndex: Int) {
        val movieCds = movieList.map { it.movie.movieCd }
        val movieTitles = movieList.map { it.movie.movieNm }
        val posterUrls = movieList.map { it.posterUrl }

        val fragment = MovieDetailPagerFragment.newInstance(
            movieCds, movieTitles, posterUrls, currentIndex
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack("MovieDetailPager")
            .commit()
    }
}