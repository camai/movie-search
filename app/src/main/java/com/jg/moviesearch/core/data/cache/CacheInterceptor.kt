package com.jg.moviesearch.core.data.cache

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * 캐시 헤더를 추가하는 인터셉터
 * API 응답에 캐시 정보가 없는 경우 강제로 캐시 헤더를 추가
 */
class CacheInterceptor : Interceptor {
    
    companion object {
        // 캐시 지속 시간 설정
        private val CACHE_DURATION_MINUTES = TimeUnit.MINUTES.toSeconds(5) // 5분
        private val CACHE_DURATION_HOURS = TimeUnit.HOURS.toSeconds(1) // 1시간
    }
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        
        // URL 기반으로 캐시 정책 설정
        val cacheControl = when {
            // 영화 검색 - 5분 캐시
            request.url.encodedPath.contains("searchMovieList") -> 
                "public, max-age=$CACHE_DURATION_MINUTES"
            
            // 영화 상세 정보 - 1시간 캐시
            request.url.encodedPath.contains("searchMovieInfo") -> 
                "public, max-age=$CACHE_DURATION_HOURS"
            
            // 포스터 이미지 - 1시간 캐시
            request.url.host.contains("tmdb") -> 
                "public, max-age=$CACHE_DURATION_HOURS"
            
            // 박스오피스 - 5분 캐시
            request.url.encodedPath.contains("BoxOffice") -> 
                "public, max-age=$CACHE_DURATION_MINUTES"
            
            // 기본값 - 5분 캐시
            else -> "public, max-age=$CACHE_DURATION_MINUTES"
        }
        
        return response.newBuilder()
            .removeHeader("Cache-Control")
            .removeHeader("Pragma")
            .header("Cache-Control", cacheControl)
            .build()
    }
} 