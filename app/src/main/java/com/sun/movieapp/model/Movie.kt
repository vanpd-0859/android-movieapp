package com.sun.movieapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.sun.movieapp.utils.Constants.FAVORITE_MOVIE_TABLE
import java.io.Serializable
import java.util.Date

@Entity(tableName = FAVORITE_MOVIE_TABLE)
data class Movie(
    @field:PrimaryKey
    val id: Int,
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: Date,
    val liked: Boolean = false
): Serializable
