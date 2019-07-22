package com.sun.movieapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_genres")
data class Genre(
    @field:PrimaryKey
    val id: Int,
    val name: String,
    var isSelected: Boolean = false
)
