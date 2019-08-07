package com.sun.movieapp.repository

import com.sun.movieapp.base.BaseRepository
import com.sun.movieapp.model.*
import com.sun.movieapp.network.MovieResponse
import com.sun.movieapp.network.MovieService
import io.reactivex.Completable
import io.reactivex.Single
import java.util.stream.Collectors

class MovieRepository(
    private val mMovieService: MovieService,
    private val mMovieDao: MovieDao
): BaseRepository {
    fun getUpcomingMovies(page: Int): Single<List<Movie>> = mMovieService.getUpcomingMovies(page).map { it.movies }

    fun getPopularMovies(page: Int): Single<List<Movie>> = mMovieService.getPopularMovies(page).map { it.movies }

    fun getSavedMovies(): Single<List<Movie>> {
        return mMovieDao.getAllMovies()
    }

    fun updateMovie(movie: Movie): Completable {
        return mMovieDao.updateMovie(movie)
    }

    fun insertMovie(movie: Movie): Completable {
        return mMovieDao.insertMovies(movie)
    }

    fun getMovieDetail(movieId: Int): Single<MovieDetail> = mMovieService.getMovieDetail(movieId)
    fun getCast(movieId: Int): Single<List<Actor>> = mMovieService.getCast(movieId).map { it.cast }
    fun getVideoOrNull(movieId: Int): Single<Video?> = mMovieService.getVideos(movieId).map { it.videos?.firstOrNull() }
    fun searchMovie(query: String, page: Int = 1): Single<MovieResponse> = mMovieService.searchMovie(query, page)

    fun searchMovieByGenre(genreIdList: List<Int>, page: Int = 1): Single<MovieResponse> {
        val stringIdList = genreIdList.stream().map { "$it" }.collect(Collectors.joining(","))
        return mMovieService.searchMovieByGenre(stringIdList, page)
    }
}
