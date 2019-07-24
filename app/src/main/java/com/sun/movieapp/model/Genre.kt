package com.sun.movieapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sun.movieapp.utils.Constants.FAVORITE_GENRE_TABLE

@Entity(tableName = FAVORITE_GENRE_TABLE)
data class Genre(
    @field:PrimaryKey
    val id: Int,
    val name: String,
    var isSelected: Boolean = false
)
