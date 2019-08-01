package com.sun.movieapp.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class MovieDetail(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: Date,
    @SerializedName("backdrop_path")
    val backDropPath: String
)
