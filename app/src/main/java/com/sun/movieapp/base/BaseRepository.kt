package com.sun.movieapp.base

import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.sun.movieapp.model.database.AppDatabase
import com.sun.movieapp.network.GenreService
import com.sun.movieapp.network.MovieService
import com.sun.movieapp.network.Network
import com.sun.movieapp.repository.GenreRepository
import com.sun.movieapp.repository.MovieRepository
import com.sun.movieapp.utils.Constants.DATABASE_NAME

interface BaseRepository {
    companion object {
        fun getGenreRepository(activity: AppCompatActivity): GenreRepository {
            val db = Room.databaseBuilder(
                activity.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME).build()
            return GenreRepository(Network.create(GenreService::class.java), db.genreDao())
        }

        fun getMovieRepository(activity: AppCompatActivity): MovieRepository {
            val db = Room.databaseBuilder(
                activity.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME).build()
            return MovieRepository(Network.create(MovieService::class.java), db.movieDao())
        }
    }
}
