package com.sun.movieapp.model

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MovieDao {
    @Query("SELECT * FROM favorite_movies ORDER BY releaseDate DESC")
    fun getAllMovies(): Single<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(vararg movies: Movie): Completable

    @Query("DELETE FROM favorite_movies")
    fun deleteAllMovies(): Completable

    @Update
    fun updateMovie(movie: Movie): Completable

    @Query("SELECT * FROM favorite_movies WHERE id = :movieId")
    fun getMovie(movieId: Int): Single<Movie>
}
