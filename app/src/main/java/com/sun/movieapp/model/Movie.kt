package com.sun.movieapp.model

import androidx.recyclerview.widget.DiffUtil
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
    var title: String = "",
    @SerializedName("vote_average")
    var voteAverage: Double = 0.0,
    @SerializedName("poster_path")
    var posterPath: String = "",
    @SerializedName("release_date")
    var releaseDate: String = "",
    var isLiked: Boolean = false
): Serializable {
    companion object {
        val mDiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.voteAverage == newItem.voteAverage &&
                        oldItem.releaseDate == newItem.releaseDate &&
                        oldItem.posterPath == newItem.posterPath &&
                        oldItem.isLiked == newItem.isLiked
            }
        }
    }
}
