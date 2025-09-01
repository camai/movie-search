package com.jg.moviesearch.ui.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jg.moviesearch.R
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.databinding.ActivityMovieDetailPageBinding
import com.jg.moviesearch.ui.model.MovieDetailPageItemUiState
import com.jg.moviesearch.ui.utils.MoviePageDiffCallback

class MovieDetailPagerAdapter(
    private val onFavoriteClick: (position: Int) -> Unit
) : ListAdapter<MovieDetailPageItemUiState, MovieDetailPagerAdapter.MovieDetailViewHolder>(MoviePageDiffCallback()) {

    private var items: List<MovieDetailPageItemUiState> = emptyList()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieDetailViewHolder {
        val binding = ActivityMovieDetailPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieDetailViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MovieDetailViewHolder(private val binding: ActivityMovieDetailPageBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnFavorite.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                   onFavoriteClick(adapterPosition)
                }
            }
        }

        fun bind(item: MovieDetailPageItemUiState) {
            binding.tvMovieTitle.text = item.movieTitle

            binding.progressBar.visibility = if (item.isLoading) View.VISIBLE else View.GONE

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

            item.movieDetail?.let { detail ->
                updateMovieDetailUI(movieDetail = detail)
            }

            binding.btnFavorite.setImageResource(
                if (item.isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outline)
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