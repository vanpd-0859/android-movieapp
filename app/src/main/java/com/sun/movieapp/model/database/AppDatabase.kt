package com.sun.movieapp.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sun.movieapp.model.Genre
import com.sun.movieapp.model.GenreDao
import com.sun.movieapp.model.Movie
import com.sun.movieapp.model.MovieDao

@Database(entities = [Genre::class, Movie::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun movieDao(): MovieDao
}
