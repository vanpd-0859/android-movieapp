package com.sun.movieapp.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<MovieResponse>
}
