package com.sun.movieapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MovieDao {
    @Query("SELECT * FROM favorite_movies ORDER BY releaseDate DESC")
    fun getAllMovies(): Single<List<Movie>>

    @Insert
    fun insertMovies(vararg movies: Movie): Completable

    @Query("DELETE FROM favorite_movies")
    fun deleteAllMovies(): Completable

    @Update
    fun updateMovie(movie: Movie): Completable
}
