package com.jg.moviesearch.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.databinding.ActivityMovieDetailBinding
import com.jg.moviesearch.ui.model.MovieData
import com.jg.moviesearch.ui.model.MovieDetailAction
import com.jg.moviesearch.ui.model.MovieDetailUiState
import com.jg.moviesearch.ui.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private var currentPosition: Int = 0
    private var movieData: MovieData = MovieData.EMPTY
    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel 초기화
        viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]

        extractIntentData()
        setupViewPager()
        setupClickListeners()
        initializeStateObservation()
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

        movieData.apply {
            binding.viewPager.adapter = createPagerAction()
            binding.viewPager.setCurrentItem(currentPosition, false)

        }
    }

    private fun createPagerAction(): MovieDetailPagerAdapter {
        return MovieDetailPagerAdapter(
            movieCds = movieData.movieCds,
            movieTitles = movieData.movieTitles,
            posterUrls = movieData.posterUrls,
            onAction = { action -> handlePagerAction(action) }
        )
    }

    private fun handlePagerAction(action: MovieDetailAction) {
        when (action) {
            is MovieDetailAction.GetMovieDetail ->{
                viewModel.getMovieDetail(action.movieCd)
            }

            is MovieDetailAction.ObserveFavoriteStatus ->
                viewModel.observeFavoriteStatus(action.movieCd)

            is MovieDetailAction.ToggleFavorite ->
                viewModel.toggleFavorite(
                    movieCd = action.movieCd,
                    movieTitle = action.movieTitle,
                    posterUrl = action.posterUrl
                )
        }
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
                    viewModel.uiState.collect { uiState ->
                        updateUiState(uiState = uiState)
                    }
                }
            }
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.movieDetail.collect { movieDetail ->
                        updateMovieDetail(movieDetail = movieDetail)
                    }
                }
            }
        }
    }

    private fun updateUiState(uiState: MovieDetailUiState) {
        if (uiState == MovieDetailUiState.EMPTY) return

        showErrorIfNeeded(error = uiState.error)
    }

    private fun showErrorIfNeeded(error: String?) {
        error?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMovieDetail(movieDetail: MovieDetail?) {
        val currentPosition = binding.viewPager.currentItem
        (binding.viewPager.adapter as? MovieDetailPagerAdapter)?.updateCurrentPageMovieDetail(
            movieDetail,
            currentPosition
        )
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