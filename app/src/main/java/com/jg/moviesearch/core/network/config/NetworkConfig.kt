package com.jg.moviesearch.core.network.config

import com.jg.moviesearch.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConfig @Inject constructor() {
    
    // KOBIS API Configuration
    val kobisBaseUrl: String = BuildConfig.KOBIS_BASE_URL
    
    // TMDB API Configuration  
    val tmdbBaseUrl: String = BuildConfig.TMDB_BASE_URL
    val tmdbImageBaseUrl: String = BuildConfig.TMDB_IMAGE_BASE_URL
    val tmdbBackdropBaseUrl: String = BuildConfig.TMDB_BACKDROP_BASE_URL
    
    init {
        // Validation to ensure URLs are properly configured
        require(kobisBaseUrl.isNotBlank()) { "KOBIS Base URL must not be blank" }
        require(tmdbBaseUrl.isNotBlank()) { "TMDB Base URL must not be blank" }
        require(tmdbImageBaseUrl.isNotBlank()) { "TMDB Image Base URL must not be blank" }
        require(tmdbBackdropBaseUrl.isNotBlank()) { "TMDB Backdrop Base URL must not be blank" }
    }
}