package com.sun.movieapp.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class MovieDetail(
    var id: Int,
    var title: String = "",
    var overview: String = "",
    @SerializedName("vote_average")
    var voteAverage: Double = 0.0,
    @SerializedName("poster_path")
    var posterPath: String = "",
    @SerializedName("release_date")
    var releaseDate: String = "",
    @SerializedName("backdrop_path")
    var backDropPath: String = ""
)
