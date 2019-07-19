package com.sun.movieapp.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sun.movieapp.model.Genre
import com.sun.movieapp.model.GenreDao

@Database(entities = [Genre::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun genreDao(): GenreDao
}
