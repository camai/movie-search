package com.jg.moviesearch.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.jg.moviesearch.R
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.databinding.ActivityMovieDetailBinding
import com.jg.moviesearch.databinding.ActivityMovieDetailPageBinding
import com.jg.moviesearch.ui.activity.MovieDetailPagerAdapter
import com.jg.moviesearch.ui.model.MovieData
import com.jg.moviesearch.ui.model.MovieDetailAction
import com.jg.moviesearch.ui.viewmodel.MovieDetailUiState
import com.jg.moviesearch.ui.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private var currentPosition: Int = 0
    
    private var movieData: MovieData = MovieData.EMPTY
    private lateinit var viewModel: MovieDetailViewModel

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel 초기화
        viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]
        
        extractIntentData()
        setupViewPager()
        setupClickListeners()
        observeUiState()
    }

    private fun extractIntentData() {
        movieData = MovieData(
            movieCds = intent.getStringArrayListExtra(EXTRA_MOVIE_CDS) ?: emptyList(),
            movieTitles = intent.getStringArrayListExtra(EXTRA_MOVIE_TITLES) ?: emptyList(),
            posterUrls = intent.getStringArrayListExtra(EXTRA_POSTER_URLS)
                ?.map { if (it.isEmpty()) null else it } ?: emptyList()
        )
        currentPosition = intent.getIntExtra(EXTRA_CURRENT_POSITION, 0)
    }

    private fun setupViewPager() {
        movieData?.let { data ->
            binding.viewPager.adapter = MovieDetailPagerAdapter(
                movieCds = data.movieCds,
                movieTitles = data.movieTitles,
                posterUrls = data.posterUrls,
                onAction = { action ->
                    when (action) {
                        is MovieDetailAction.GetMovieDetail -> viewModel.getMovieDetail(movieCd = action.movieCd)
                        is MovieDetailAction.ObserveFavoriteStatus -> viewModel.observeFavoriteStatus(
                            movieCd = action.movieCd
                        )
                        is MovieDetailAction.ToggleFavorite -> viewModel.toggleFavorite(
                            movieCd = action.movieCd,
                            movieTitle = action.movieTitle,
                            posterUrl = action.posterUrl
                        )
                    }
                }
            )
            binding.viewPager.setCurrentItem(currentPosition, false)
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
    
    private fun observeUiState() {
        viewModel.uiState.observe(this) { uiState ->
            // 로딩 상태 업데이트 (현재 페이지의 progressBar)
            updateProgressBar(isLoading = uiState.isLoading)
            
            // 에러 처리
            uiState.error?.let { error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
            
            // 영화 상세 정보 업데이트
            uiState.movieDetail?.let { movieDetail ->
                updateCurrentPageUI(movieDetail = movieDetail)
            }
            
            // 즐겨찾기 상태 업데이트
            updateFavoriteButton(isFavorite = uiState.isFavorite)
        }
    }
    
    private fun updateCurrentPageUI(movieDetail: MovieDetail) {
        withCurrentPage { pageBinding ->
            pageBinding.apply {
                tvMovieTitle.text = movieDetail.movieNm
                tvMovieType.text = movieDetail.typeNm
                tvOpenDate.text = movieDetail.openDt
                tvShowTime.text = "${movieDetail.showTm}분"
                tvDirector.text = movieDetail.directors.joinToString(", ") { it.peopleNm }
                tvActors.text = movieDetail.actors.joinToString(", ") { it.peopleNm }
                tvGenre.text = movieDetail.genres.joinToString(", ") { it.genreNm }
                tvNation.text = movieDetail.nations.joinToString(", ") { it.nationNm }
                tvWatchGrade.text = movieDetail.prdtStatNm
            }
        }
    }
    
    private fun withCurrentPage(action: (ActivityMovieDetailPageBinding) -> Unit) {
        binding.viewPager.getChildAt(0)?.let { currentPage ->
            val pageBinding = ActivityMovieDetailPageBinding.bind(currentPage)
            action(pageBinding)
        }
    }

    private fun updateProgressBar(isLoading: Boolean) {
        withCurrentPage { pageBinding ->
            pageBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
    
    private fun updateFavoriteButton(isFavorite: Boolean) {
        withCurrentPage { pageBinding ->
            pageBinding.btnFavorite.setImageResource(
                if (isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
            )
        }
    }
}