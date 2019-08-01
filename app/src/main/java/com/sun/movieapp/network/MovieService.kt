package com.sun.movieapp.network

import com.sun.movieapp.model.MovieDetail
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/{id}")
    fun getMovieDetail(@Path("id") movieId: Int): Single<MovieDetail>

    @GET("movie/{id}/credits")
    fun getCast(@Path("id") movieId: Int): Single<CastResponse>

    @GET("movie/{id}/videos")
    fun getVideos(@Path("id") movieId: Int): Single<VideoResponse>

    @GET("search/movie")
    fun searchMovie(@Query("query") query: String, @Query("page") page: Int): Single<MovieResponse>

    @GET("discover/movie")
    fun searchMovieByGenre(@Query("with_genres") genreIdList: String, @Query("page") page: Int): Single<MovieResponse>
}
