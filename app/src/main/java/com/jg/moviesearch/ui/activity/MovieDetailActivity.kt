package com.jg.moviesearch.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.jg.moviesearch.R
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.ui.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnBack: ImageButton
    private lateinit var movieCds: List<String>
    private lateinit var movieTitles: List<String>
    private lateinit var posterUrls: List<String?>
    private var currentPosition: Int = 0

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
        setContentView(R.layout.activity_movie_detail)

        extractIntentData()
        setupViews()
        setupViewPager()
        setupClickListeners()
    }

    private fun extractIntentData() {
        movieCds = intent.getStringArrayListExtra(EXTRA_MOVIE_CDS) ?: emptyList()
        movieTitles = intent.getStringArrayListExtra(EXTRA_MOVIE_TITLES) ?: emptyList()
        posterUrls = intent.getStringArrayListExtra(EXTRA_POSTER_URLS)
            ?.map { url -> if (url.isEmpty()) null else url } ?: emptyList()
        currentPosition = intent.getIntExtra(EXTRA_CURRENT_POSITION, 0)
    }

    private fun setupViews() {
        viewPager = findViewById(R.id.viewPager)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun setupViewPager() {
        viewPager.adapter = MovieDetailPagerAdapter()
        viewPager.setCurrentItem(currentPosition, false)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    inner class MovieDetailPagerAdapter :
        RecyclerView.Adapter<MovieDetailPagerAdapter.MovieDetailViewHolder>() {

        override fun getItemCount(): Int = movieCds.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDetailViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_movie_detail_page, parent, false)
            return MovieDetailViewHolder(view)
        }

        override fun onBindViewHolder(holder: MovieDetailViewHolder, position: Int) {
            holder.bind(movieCds[position], movieTitles[position], posterUrls[position])
        }

        inner class MovieDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
            private val ivPoster: ImageView = itemView.findViewById(R.id.ivPoster)
            private val tvMovieTitle: TextView = itemView.findViewById(R.id.tvMovieTitle)
            private val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)
            private val tvOpenDate: TextView = itemView.findViewById(R.id.tvOpenDate)
            private val tvShowTime: TextView = itemView.findViewById(R.id.tvShowTime)
            private val tvMovieType: TextView = itemView.findViewById(R.id.tvMovieType)
            private val tvNation: TextView = itemView.findViewById(R.id.tvNation)
            private val tvGenre: TextView = itemView.findViewById(R.id.tvGenre)
            private val tvWatchGrade: TextView = itemView.findViewById(R.id.tvWatchGrade)
            private val tvDirector: TextView = itemView.findViewById(R.id.tvDirector)
            private val tvActors: TextView = itemView.findViewById(R.id.tvActors)
            private val tvPrdtYear: TextView = itemView.findViewById(R.id.tvPrdtYear)

            private var viewModel: MovieDetailViewModel? = null

            fun bind(movieCd: String, movieTitle: String, posterUrl: String?) {
                // ViewModel 초기화
                viewModel = ViewModelProvider(this@MovieDetailActivity)[MovieDetailViewModel::class.java]

                // 기본 정보 설정
                tvMovieTitle.text = movieTitle

                // 포스터 이미지 로드
                posterUrl?.let { url ->
                    if (url.isNotEmpty()) {
                        ivPoster.load(url) {
                            placeholder(R.drawable.ic_movie_placeholder)
                            error(R.drawable.ic_movie_placeholder)
                        }
                    } else {
                        ivPoster.setImageResource(R.drawable.ic_movie_placeholder)
                    }
                } ?: run {
                    ivPoster.setImageResource(R.drawable.ic_movie_placeholder)
                }

                // 즐겨찾기 버튼 클릭
                btnFavorite.setOnClickListener {
                    viewModel?.toggleFavorite(movieCd, movieTitle, posterUrl)
                }

                // ViewModel의 UI 상태 관찰
                viewModel?.let { vm ->
                    vm.uiState.observe(this@MovieDetailActivity) { uiState ->
                        uiState.movieDetail?.let { updateUI(it) }
                        progressBar.visibility = if (uiState.isLoading) View.VISIBLE else View.GONE
                        uiState.error?.let { error ->
                            Toast.makeText(this@MovieDetailActivity, error, Toast.LENGTH_SHORT).show()
                        }
                        btnFavorite.setImageResource(
                            if (uiState.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline
                        )
                    }

                    // 영화 상세 정보 로드
                    if (movieCd.isNotEmpty()) {
                        vm.getMovieDetail(movieCd)
                        vm.observeFavoriteStatus(movieCd)
                    }
                }
            }

            private fun updateUI(movieDetail: MovieDetail) {
                tvMovieTitle.text = movieDetail.movieNm
                tvMovieType.text = movieDetail.typeNm
                tvOpenDate.text = movieDetail.openDt
                tvShowTime.text = "${movieDetail.showTm}분"
                tvDirector.text = movieDetail.directors.joinToString(", ") { it.peopleNm }
                tvActors.text = movieDetail.actors.joinToString(", ") { it.peopleNm }
                tvGenre.text = movieDetail.genres.joinToString(", ") { it.genreNm }
                tvNation.text = movieDetail.nations.joinToString(", ") { it.nationNm }
                tvWatchGrade.text = movieDetail.prdtStatNm // 제작상태
                tvPrdtYear.text = movieDetail.prdtYear // 제작연도
            }
        }
    }
}