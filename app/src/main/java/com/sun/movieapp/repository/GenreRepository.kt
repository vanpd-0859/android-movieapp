package com.sun.movieapp.repository

import com.sun.movieapp.base.BaseRepository
import com.sun.movieapp.model.Genre
import com.sun.movieapp.network.GenreService
import io.reactivex.Single

class GenreRepository(
    private val mGenreService: GenreService
): BaseRepository {
    fun getGenres(): Single<List<Genre>> {
        return mGenreService.getGenres().map { it.genres }
    }
}
