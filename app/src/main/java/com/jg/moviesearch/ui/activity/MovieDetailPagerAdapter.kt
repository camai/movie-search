package com.jg.moviesearch.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jg.moviesearch.R
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

    override fun onBindViewHolder(holder: MovieDetailViewHolder, position: Int) {
        holder.bind(movieCds[position], movieTitles[position], posterUrls[position])
    }

    inner class MovieDetailViewHolder(private val binding: ActivityMovieDetailPageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movieCd: String, movieTitle: String, posterUrl: String?) {
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

            binding.btnFavorite.setOnClickListener {
                onAction(MovieDetailAction.ToggleFavorite(movieCd, movieTitle, posterUrl))
            }

            if (movieCd.isNotEmpty()) {
                onAction(MovieDetailAction.GetMovieDetail(movieCd))
                onAction(MovieDetailAction.ObserveFavoriteStatus(movieCd))
            }
        }
    }
}