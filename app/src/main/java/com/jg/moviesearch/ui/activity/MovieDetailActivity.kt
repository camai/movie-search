package com.jg.moviesearch.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.databinding.ActivityMovieDetailBinding
import com.jg.moviesearch.ui.model.MovieData
import com.jg.moviesearch.ui.model.MovieDetailUiState
import com.jg.moviesearch.ui.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.viewpager2.widget.ViewPager2
import com.jg.moviesearch.ui.model.MovieDetailUiEffect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private var currentPosition: Int = 0
    private var movieData: MovieData = MovieData.EMPTY
    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var pagerAdapter: MovieDetailPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]

        extractIntentData()

        if (savedInstanceState == null) {
            viewModel.initializePages(movie = movieData, initialPosition = currentPosition)
        }

        setupViewPager()
        setupClickListeners()
        initializeStateObservation()
        loadInitialMovieDetail()
    }

    private fun extractIntentData() {
        movieData = MovieData(
            movieCds = intent.getStringArrayListExtra(EXTRA_MOVIE_CDS) ?: emptyList(),
            movieTitles = intent.getStringArrayListExtra(EXTRA_MOVIE_TITLES) ?: emptyList(),
            posterUrls = intent.getStringArrayListExtra(EXTRA_POSTER_URLS)
                ?.map { it.ifEmpty { null } } ?: emptyList()
        )
        currentPosition = intent.getIntExtra(EXTRA_CURRENT_POSITION, 0)
    }

    private fun setupViewPager() {
        if (movieData == MovieData.EMPTY) return

        pagerAdapter = MovieDetailPagerAdapter { position ->
            viewModel.toggleFavorite(position = position)
        }

        binding.viewPager.apply {
            adapter = pagerAdapter
            setCurrentItem(currentPosition, false)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.loadPageDetails(position = position)
                }
            })
        }
    }

    private fun loadInitialMovieDetail() {
        viewModel.initializePages(movieData, currentPosition)
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initializeStateObservation() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState
                        .map { it.pages } // 페이지 리스트가 변경 될 때만
                        .distinctUntilChanged()
                        .collect { uiState ->
                            pagerAdapter.submitList(uiState)
                        }
                }

                launch {
                    viewModel.uiState
                        .map { it.currentPosition } // 현재 위치가 변결될 때만
                        .distinctUntilChanged()
                        .collect { position ->
                            binding.viewPager.setCurrentItem(position, false)
                        }
                }

                launch {
                    viewModel.uiEffect.collect { uiEffect ->
                        when (uiEffect) {
                            is MovieDetailUiEffect.ShowError -> {
                                Toast.makeText(this@MovieDetailActivity, uiEffect.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val EXTRA_MOVIE_CDS = "extra_movie_cds"
        private const val EXTRA_MOVIE_TITLES = "extra_movie_titles"
        private const val EXTRA_POSTER_URLS = "extra_poster_urls"
        private const val EXTRA_CURRENT_POSITION = "extra_current_position"

        fun newIntent(
            context: Context,
            movieCds: List<String>,
            movieTitles: List<String>,
            posterUrls: List<String?>,
            currentPosition: Int
        ): Intent {
            return Intent(context, MovieDetailActivity::class.java).apply {
                putStringArrayListExtra(EXTRA_MOVIE_CDS, ArrayList(movieCds))
                putStringArrayListExtra(EXTRA_MOVIE_TITLES, ArrayList(movieTitles))
                putStringArrayListExtra(EXTRA_POSTER_URLS, ArrayList(posterUrls.map { it ?: "" }))
                putExtra(EXTRA_CURRENT_POSITION, currentPosition)
            }
        }
    }
}