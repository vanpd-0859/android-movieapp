package com.sun.movieapp.repository

import com.sun.movieapp.base.BaseRepository
import com.sun.movieapp.model.Genre
import com.sun.movieapp.model.GenreDao
import com.sun.movieapp.network.GenreService
import io.reactivex.Completable
import io.reactivex.Single

class GenreRepository(
    private val mGenreService: GenreService,
    private val mGenreDao: GenreDao
): BaseRepository {
    fun getGenres(): Single<List<Genre>> = mGenreService.getGenres().map { it.genres }

    fun saveFavoriteGenres(genres: List<Genre>): Completable = mGenreDao.insertGenres(*genres.toTypedArray())

    fun getFavoriteGenres(): Single<List<Genre>> = mGenreDao.getAllGenres()

    fun deleteAllFavoritesGenres(): Completable = mGenreDao.deleteAllGenres()
}
