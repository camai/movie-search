package com.jg.moviesearch.core.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jg.moviesearch.core.database.dao.FavoriteMovieDao
import com.jg.moviesearch.core.database.entity.FavoriteMovieEntity

/**
 * 영화 검색 앱의 메인 데이터베이스
 * 즐겨찾기 영화 정보를 저장하는 Room 데이터베이스
 */
@Database(
    entities = [FavoriteMovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {

    /**
     * 즐겨찾기 영화 DAO 추상 함수
     * @return FavoriteMovieDao 인스턴스
     */
    abstract fun favoriteMovieDao(): FavoriteMovieDao

    companion object {
        // 데이터베이스 이름
        private const val DATABASE_NAME = "movie_database"

        // 싱글톤 인스턴스
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        /**
         * 데이터베이스 인스턴스 가져오기
         * @param context 애플리케이션 컨텍스트
         * @return MovieDatabase 인스턴스
         */
        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}