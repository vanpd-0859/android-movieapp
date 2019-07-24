package com.sun.movieapp.network

import io.reactivex.Single
import retrofit2.http.GET

interface GenreService {
    @GET("genre/movie/list")
    fun getGenres(): Single<GenreResponse>
}
