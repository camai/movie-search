package com.jg.moviesearch.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.jg.moviesearch.R
import com.jg.moviesearch.databinding.FragmentMovieDetailBinding
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.ui.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: MovieDetailViewModel by viewModels()
    
    private var movieCd: String = ""
    private var movieTitle: String = ""
    private var posterUrl: String? = null
    
    companion object {
        private const val ARG_MOVIE_CD = "arg_movie_cd"
        private const val ARG_MOVIE_TITLE = "arg_movie_title"
        private const val ARG_POSTER_URL = "arg_poster_url"
        
        fun newInstance(movieCd: String, movieTitle: String, posterUrl: String? = null): MovieDetailFragment {
            return MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MOVIE_CD, movieCd)
                    putString(ARG_MOVIE_TITLE, movieTitle)
                    putString(ARG_POSTER_URL, posterUrl)
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieCd = it.getString(ARG_MOVIE_CD) ?: ""
            movieTitle = it.getString(ARG_MOVIE_TITLE) ?: ""
            posterUrl = it.getString(ARG_POSTER_URL)
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupClickListeners()
        observeViewModel()
        loadMovieDetail()
    }
    
    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            // Fragment 백스택에서 제거하여 이전 화면으로 돌아가기
            parentFragmentManager.popBackStack()
        }
    }
    
    private fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer { uiState ->
            handleLoadingState(uiState.isLoading)
            handleErrorState(uiState.error)
            uiState.movieDetail?.let { movieDetail ->
                displayMovieDetail(movieDetail)
            }
        })
    }
    
    private fun loadMovieDetail() {
        if (movieCd.isNotEmpty()) {
            viewModel.getMovieDetail(movieCd)
        } else {
            Toast.makeText(context, "영화 정보가 없습니다", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
    
    private fun handleLoadingState(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    
    private fun handleErrorState(error: String?) {
        if (error != null) {
            binding.tvErrorMessage.text = error
            binding.tvErrorMessage.visibility = View.VISIBLE
            hideAllViews()
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        } else {
            binding.tvErrorMessage.visibility = View.GONE
            showAllViews()
        }
    }
    
    private fun displayMovieDetail(movieDetail: MovieDetail) {
        with(binding) {
            // 포스터 이미지 로드
            posterUrl?.let { url ->
                ivMoviePoster.load(url) {
                    placeholder(R.drawable.ic_launcher_background)
                    error(R.drawable.ic_launcher_background)
                }
            }
            
            // 기본 정보
            tvMovieTitle.text = movieDetail.movieNm
            tvMovieTitleEn.text = movieDetail.movieNmEn
            tvOpenDate.text = formatDate(movieDetail.openDt)
            tvShowTime.text = if (movieDetail.showTm.isNotEmpty()) "${movieDetail.showTm}분" else "정보 없음"
            tvProductionYear.text = movieDetail.prdtYear
            tvProductionStatus.text = movieDetail.prdtStatNm
            tvMovieType.text = movieDetail.typeNm
            
            // 제작국가
            val nations = movieDetail.nations.joinToString(", ") { it.nationNm }
            tvNations.text = nations.ifEmpty { "정보 없음" }
            
            // 장르
            val genres = movieDetail.genres.joinToString(", ") { it.genreNm }
            tvGenres.text = genres.ifEmpty { "정보 없음" }
            
            // 감독
            val directors = movieDetail.directors.joinToString(", ") { it.peopleNm }
            tvDirectors.text = directors.ifEmpty { "정보 없음" }
            
            // 배우
            val actors = movieDetail.actors.joinToString(", ") { "${it.peopleNm} (${it.cast})" }
            tvActors.text = actors.ifEmpty { "정보 없음" }
        }
    }
    
    private fun formatDate(date: String): String {
        return if (date.length == 8) {
            "${date.substring(0, 4)}-${date.substring(4, 6)}-${date.substring(6, 8)}"
        } else {
            date
        }
    }
    
    private fun hideAllViews() {
        with(binding) {
            ivMoviePoster.visibility = View.GONE
            tvMovieTitle.visibility = View.GONE
            tvMovieTitleEn.visibility = View.GONE
            divider1.visibility = View.GONE
            tvBasicInfoLabel.visibility = View.GONE
            tvOpenDateLabel.visibility = View.GONE
            tvOpenDate.visibility = View.GONE
            tvShowTimeLabel.visibility = View.GONE
            tvShowTime.visibility = View.GONE
            tvProductionYearLabel.visibility = View.GONE
            tvProductionYear.visibility = View.GONE
            tvProductionStatusLabel.visibility = View.GONE
            tvProductionStatus.visibility = View.GONE
            tvMovieTypeLabel.visibility = View.GONE
            tvMovieType.visibility = View.GONE
            tvNationsLabel.visibility = View.GONE
            tvNations.visibility = View.GONE
            tvGenresLabel.visibility = View.GONE
            tvGenres.visibility = View.GONE
            divider2.visibility = View.GONE
            tvDirectorsLabel.visibility = View.GONE
            tvDirectors.visibility = View.GONE
            tvActorsLabel.visibility = View.GONE
            tvActors.visibility = View.GONE
        }
    }
    
    private fun showAllViews() {
        with(binding) {
            ivMoviePoster.visibility = View.VISIBLE
            tvMovieTitle.visibility = View.VISIBLE
            tvMovieTitleEn.visibility = View.VISIBLE
            divider1.visibility = View.VISIBLE
            tvBasicInfoLabel.visibility = View.VISIBLE
            tvOpenDateLabel.visibility = View.VISIBLE
            tvOpenDate.visibility = View.VISIBLE
            tvShowTimeLabel.visibility = View.VISIBLE
            tvShowTime.visibility = View.VISIBLE
            tvProductionYearLabel.visibility = View.VISIBLE
            tvProductionYear.visibility = View.VISIBLE
            tvProductionStatusLabel.visibility = View.VISIBLE
            tvProductionStatus.visibility = View.VISIBLE
            tvMovieTypeLabel.visibility = View.VISIBLE
            tvMovieType.visibility = View.VISIBLE
            tvNationsLabel.visibility = View.VISIBLE
            tvNations.visibility = View.VISIBLE
            tvGenresLabel.visibility = View.VISIBLE
            tvGenres.visibility = View.VISIBLE
            divider2.visibility = View.VISIBLE
            tvDirectorsLabel.visibility = View.VISIBLE
            tvDirectors.visibility = View.VISIBLE
            tvActorsLabel.visibility = View.VISIBLE
            tvActors.visibility = View.VISIBLE
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 