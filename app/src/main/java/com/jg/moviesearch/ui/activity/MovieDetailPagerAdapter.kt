package com.jg.moviesearch.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jg.moviesearch.R
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.databinding.ActivityMovieDetailPageBinding
import com.jg.moviesearch.ui.model.MovieDetailAction

class MovieDetailPagerAdapter(
    private val movieCds: List<String>,
    private val movieTitles: List<String>,
    private val posterUrls: List<String?>,
    private val onAction: (MovieDetailAction) -> Unit
) : RecyclerView.Adapter<MovieDetailPagerAdapter.MovieDetailViewHolder>() {

    override fun getItemCount(): Int = movieCds.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDetailViewHolder {
        val binding = ActivityMovieDetailPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieDetailViewHolder(binding)
    }

    private var currentMovieDetail: MovieDetail? = null
    private var currentPagePosition: Int = 0

    // Activity에서 호출할 public 메서드
    fun updateCurrentPageMovieDetail(movieDetail: MovieDetail?, currentPosition: Int) {
        currentMovieDetail = movieDetail
        currentPagePosition = currentPosition
        notifyItemChanged(currentPosition) // 현재 페이지만 업데이트
    }

    override fun onBindViewHolder(holder: MovieDetailViewHolder, position: Int) {
        val movieCd = movieCds.getOrNull(position) ?: ""
        val movieTitle = movieTitles.getOrNull(position) ?: ""
        val posterUrl = posterUrls.getOrNull(position)

        // 현재 페이지에만 movieDetail 전달
        val movieDetail = if (position == currentPagePosition) currentMovieDetail else null

        holder.bind(movieCd, movieTitle, posterUrl, movieDetail)
    }

    inner class MovieDetailViewHolder(private val binding: ActivityMovieDetailPageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movieCd: String, movieTitle: String, posterUrl: String?, movieDetail: MovieDetail?) {
            binding.tvMovieTitle.text = movieTitle

            posterUrl?.let { url ->
                if (url.isNotEmpty()) {
                    binding.ivPoster.load(url) {
                        placeholder(R.drawable.ic_movie_placeholder)
                        error(R.drawable.ic_movie_placeholder)
                    }
                } else {
                    binding.ivPoster.setImageResource(R.drawable.ic_movie_placeholder)
                }
            } ?: run {
                binding.ivPoster.setImageResource(R.drawable.ic_movie_placeholder)
            }

            // 영화 상세 정보 바인딩
            movieDetail?.let {
                updateMovieDetailUI(movieDetail = it)
            }

            binding.btnFavorite.setOnClickListener {
                onAction(MovieDetailAction.ToggleFavorite(
                    movieCd = movieCd,
                    movieTitle = movieTitle,
                    posterUrl = posterUrl
                ))
            }

            if (movieCd.isNotEmpty()) {
                onAction(MovieDetailAction.GetMovieDetail(movieCd = movieCd))
                onAction(MovieDetailAction.ObserveFavoriteStatus(movieCd = movieCd))
            }
        }

        private fun updateMovieDetailUI(movieDetail: MovieDetail) {
            binding.apply {
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
}