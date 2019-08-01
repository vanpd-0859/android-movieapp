package com.sun.movieapp.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sun.movieapp.utils.Constants.FAVORITE_GENRE_TABLE

@Entity(tableName = FAVORITE_GENRE_TABLE)
data class Genre(
    @field:PrimaryKey
    val id: Int,
    val name: String,
    var isSelected: Boolean = false
) {
    companion object {
        val mDiffCallback = object: DiffUtil.ItemCallback<Genre>() {
            override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.isSelected == newItem.isSelected
            }
        }
    }
}
