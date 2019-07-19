package com.sun.movieapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GenreDao {
    @Query("SELECT * FROM favorite_genres")
    fun getAllGenres(): List<Genre>

    @Insert
    fun insertGenres(vararg genres: Genre)

    @Query("DELETE FROM favorite_genres")
    fun deleteAllGenres()
}

