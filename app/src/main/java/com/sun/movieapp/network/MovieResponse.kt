package com.sun.movieapp.network

import com.google.gson.annotations.SerializedName
import com.sun.movieapp.model.Movie

data class MovieResponse(
    @SerializedName("results")
    val movies: List<Movie>
)
