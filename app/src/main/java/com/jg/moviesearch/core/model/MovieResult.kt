package com.jg.moviesearch.core.model

/**
 * 영화 검색 API 결과를 나타내는 sealed class
 * Flow 기반 Clean Architecture에서 사용
 */
sealed class MovieResult<out T> {
    /**
     * 로딩 상태
     */
    object Loading : MovieResult<Nothing>()
    
    /**
     * 성공 상태 - 데이터와 함께 반환
     */
    data class Success<T>(val data: T) : MovieResult<T>()
    
    /**
     * 에러 상태 - 예외 정보와 함께 반환
     */
    data class Error(
        val exception: Throwable,
        val message: String = exception.message ?: "Unknown error"
    ) : MovieResult<Nothing>()
    
    /**
     * 빈 결과 상태
     */
    object Empty : MovieResult<Nothing>()
}

/**
 * 성공 여부 확인
 */
fun <T> MovieResult<T>.isSuccess(): Boolean = this is MovieResult.Success

/**
 * 에러 여부 확인
 */
fun <T> MovieResult<T>.isError(): Boolean = this is MovieResult.Error

/**
 * 로딩 여부 확인
 */
fun <T> MovieResult<T>.isLoading(): Boolean = this is MovieResult.Loading

/**
 * 빈 결과 여부 확인
 */
fun <T> MovieResult<T>.isEmpty(): Boolean = this is MovieResult.Empty

/**
 * 데이터 추출 함수
 */
fun <T> MovieResult<T>.getDataOrNull(): T? {
    return when (this) {
        is MovieResult.Success -> data
        else -> null
    }
} 