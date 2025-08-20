package com.jg.moviesearch.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jg.moviesearch.R
import com.jg.moviesearch.databinding.ActivityMovieDetailPageBinding

class MovieDetailPagerAdapter(
    private val movieCds: List<String>,
    private val movieTitles: List<String>,
    private val posterUrls: List<String?>,
    private val onMovieDetailRequest: (String) -> Unit,
    private val onFavoriteStatusRequest: (String) -> Unit,
    private val onFavoriteToggle: (String, String, String?) -> Unit
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
            // 기본 정보 설정
            binding.tvMovieTitle.text = movieTitle

            // 포스터 이미지 로드
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

            // 즐겨찾기 버튼 클릭
            binding.btnFavorite.setOnClickListener {
                onFavoriteToggle(movieCd, movieTitle, posterUrl)
            }

            // 영화 상세 정보 로드
            if (movieCd.isNotEmpty()) {
                onMovieDetailRequest(movieCd)
                onFavoriteStatusRequest(movieCd)
            }
        }
    }
}