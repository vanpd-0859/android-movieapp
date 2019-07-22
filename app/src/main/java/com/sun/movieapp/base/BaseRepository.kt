package com.sun.movieapp.base

import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.sun.movieapp.model.database.AppDatabase
import com.sun.movieapp.network.GenreService
import com.sun.movieapp.network.Network
import com.sun.movieapp.repository.GenreRepository

interface BaseRepository {
    companion object {
        fun getGenreRepository(activity: AppCompatActivity): GenreRepository {
            val db = Room.databaseBuilder(
                activity.applicationContext,
                AppDatabase::class.java,
                "MovieApp").build()
            return GenreRepository(Network.create(GenreService::class.java), db.genreDao())
        }
    }
}
