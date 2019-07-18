package com.sun.movieapp.network

import com.sun.movieapp.utils.THE_MOVIE_DB_API_KEY
import io.reactivex.Single
import retrofit2.http.GET

interface GenreService {
    @GET("genre/movie/list?api_key=$THE_MOVIE_DB_API_KEY")
    fun getGenres(): Single<GenreResponse>
}
