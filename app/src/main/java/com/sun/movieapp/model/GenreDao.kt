package com.sun.movieapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface GenreDao {
    @Query("SELECT * FROM favorite_genres ORDER BY name DESC")
    fun getAllGenres(): Single<List<Genre>>

    @Insert
    fun insertGenres(vararg genres: Genre): Completable

    @Query("DELETE FROM favorite_genres")
    fun deleteAllGenres(): Completable
}

