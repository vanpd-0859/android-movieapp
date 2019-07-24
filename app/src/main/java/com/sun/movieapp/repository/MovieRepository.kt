package com.sun.movieapp.repository

import com.google.common.collect.Lists
import com.sun.movieapp.base.BaseRepository
import com.sun.movieapp.model.Movie
import com.sun.movieapp.model.MovieDao
import com.sun.movieapp.network.MovieService
import io.reactivex.Single

class MovieRepository(
    private val mMovieService: MovieService,
    private val mMovieDao: MovieDao
): BaseRepository {
    fun getUpcomingMovies(page: Int = 1): Single<List<Movie>> {
        return mMovieService.getUpcomingMovies(page).map { it.movies }
    }

    fun getPopularMovies(page: Int = 1): Single<List<Movie>> {
        return mMovieService.getPopularMovies(page).map { it.movies }
    }

    fun getFavoriteMovies(page: Int = 1, limit: Int = 10): Single<List<Movie>> {
        return mMovieDao.getAllMovies()
            .map {
                val partition = Lists.partition(it, limit)
                partition[page]
            }
    }
}
