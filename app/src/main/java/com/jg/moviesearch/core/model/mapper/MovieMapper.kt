package com.jg.moviesearch.core.model.mapper

import com.jg.moviesearch.core.model.domain.Movie
import com.jg.moviesearch.core.model.domain.MovieDetail
import com.jg.moviesearch.core.model.domain.Nation
import com.jg.moviesearch.core.model.domain.Genre
import com.jg.moviesearch.core.model.domain.Director
import com.jg.moviesearch.core.model.domain.Actor
import com.jg.moviesearch.core.model.dto.MovieDto
import com.jg.moviesearch.core.model.dto.MovieDetailDto
import com.jg.moviesearch.core.model.dto.NationDto
import com.jg.moviesearch.core.model.dto.GenreDto
import com.jg.moviesearch.core.model.dto.DirectorDto
import com.jg.moviesearch.core.model.dto.ActorDto
import com.jg.moviesearch.core.model.dto.MovieListItemDto

/**
 * Movie DTO ↔ Domain Model 변환 Mapper
 */
object MovieMapper {
    
    /**
     * MovieDto를 Movie Domain Model로 변환
     */
    fun MovieDto.toDomainModel(): Movie {
        return Movie(
            rank = this.rank,
            rankInten = this.rankInten,
            rankOldAndNew = this.rankOldAndNew,
            movieCd = this.movieCd,
            movieNm = this.movieNm,
            openDt = this.openDt,
            salesAmt = this.salesAmt,
            salesShare = this.salesShare,
            salesInten = this.salesInten,
            salesChange = this.salesChange,
            salesAcc = this.salesAcc,
            audiCnt = this.audiCnt,
            audiInten = this.audiInten,
            audiChange = this.audiChange,
            audiAcc = this.audiAcc,
            scrnCnt = this.scrnCnt,
            showCnt = this.showCnt
        )
    }
    
    /**
     * MovieListItemDto를 Movie Domain Model로 변환
     */
    fun MovieListItemDto.toDomainModel(): Movie {
        return Movie(
            rank = "0",
            rankInten = "0",
            rankOldAndNew = "NEW",
            movieCd = this.movieCd,
            movieNm = this.movieNm,
            openDt = this.openDt ?: "",
            salesAmt = "0",
            salesShare = "0.0",
            salesInten = "0",
            salesChange = "0",
            salesAcc = "0",
            audiCnt = "0",
            audiInten = "0",
            audiChange = "0",
            audiAcc = "0",
            scrnCnt = "0",
            showCnt = "0"
        )
    }
    
    /**
     * MovieDetailDto를 MovieDetail Domain Model로 변환
     */
    fun MovieDetailDto.toDomainModel(): MovieDetail {
        val nationsList = arrayListOf<Nation>()
        for (nationDto in this.nations) {
            nationsList.add(nationDto.toDomainModel())
        }
        
        val genresList = arrayListOf<Genre>()
        for (genreDto in this.genres) {
            genresList.add(genreDto.toDomainModel())
        }
        
        val directorsList = arrayListOf<Director>()
        for (directorDto in this.directors) {
            directorsList.add(directorDto.toDomainModel())
        }
        
        val actorsList = arrayListOf<Actor>()
        for (actorDto in this.actors) {
            actorsList.add(actorDto.toDomainModel())
        }
        
        return MovieDetail(
            movieCd = this.movieCd,
            movieNm = this.movieNm,
            movieNmEn = this.movieNmEn,
            prdtYear = this.prdtYear,
            showTm = this.showTm,
            openDt = this.openDt,
            prdtStatNm = this.prdtStatNm,
            typeNm = this.typeNm,
            nations = nationsList,
            genres = genresList,
            directors = directorsList,
            actors = actorsList
        )
    }
    
    /**
     * NationDto를 Nation Domain Model로 변환
     */
    fun NationDto.toDomainModel(): Nation {
        return Nation(nationNm = this.nationNm)
    }
    
    /**
     * GenreDto를 Genre Domain Model로 변환
     */
    fun GenreDto.toDomainModel(): Genre {
        return Genre(genreNm = this.genreNm)
    }
    
    /**
     * DirectorDto를 Director Domain Model로 변환
     */
    fun DirectorDto.toDomainModel(): Director {
        return Director(peopleNm = this.peopleNm)
    }
    
    /**
     * ActorDto를 Actor Domain Model로 변환
     */
    fun ActorDto.toDomainModel(): Actor {
        return Actor(
            peopleNm = this.peopleNm,
            cast = this.cast
        )
    }
} 