package com.jg.moviesearch.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jg.moviesearch.R
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.databinding.ActivityMovieDetailPageBinding
import com.jg.moviesearch.ui.model.MovieDetailAction
import com.jg.moviesearch.ui.model.MoviePageItem
import com.jg.moviesearch.ui.utils.MoviePageDiffCallback

class MovieDetailPagerAdapter(
    private val onAction: (MovieDetailAction) -> Unit
) : RecyclerView.Adapter<MovieDetailPagerAdapter.MovieDetailViewHolder>() {

    private var items: List<MoviePageItem> = emptyList()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDetailViewHolder {
        val binding = ActivityMovieDetailPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieDetailViewHolder(binding)
    }

    fun setInitialData(
        movieCds: List<String>,
        movieTitles: List<String>,
        posterUrls: List<String?>
    ) {
        val initialItems = movieCds.mapIndexed { index, movieCd ->
            MoviePageItem(
                movieCd = movieCd,
                movieTitle = movieTitles.getOrNull(index) ?: "",
                posterUrl = posterUrls.getOrNull(index),
                movieDetail = MovieDetail.EMPTY
            )
        }
        updateItems(initialItems)
    }

    fun updateItems(newItems: List<MoviePageItem>) {
        val diffCallback = MoviePageDiffCallback(items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        items = newItems
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateMovieDetail(position: Int, movieDetail: MovieDetail) {
        if (position in items.indices) {
            val newItems = items.toMutableList().apply {
                this[position] = this[position].copy(movieDetail = movieDetail)
            }
            updateItems(newItems)
        }
    }

    override fun onBindViewHolder(holder: MovieDetailViewHolder, position: Int) {
        val item = items.getOrNull(position) ?: run {
            // 로그로 문제 상황 확인
            println("Warning: Invalid position $position, items size: ${items.size}")
            return
        }

        holder.bind(item)
    }

    inner class MovieDetailViewHolder(private val binding: ActivityMovieDetailPageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MoviePageItem) {
            binding.tvMovieTitle.text = item.movieTitle

            item.posterUrl?.let { url ->
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

            updateMovieDetailUI(movieDetail = item.movieDetail)

            binding.btnFavorite.setOnClickListener {
                onAction(MovieDetailAction.ToggleFavorite(
                    movieCd = item.movieCd,
                    movieTitle = item.movieTitle,
                    posterUrl = item.posterUrl
                ))
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