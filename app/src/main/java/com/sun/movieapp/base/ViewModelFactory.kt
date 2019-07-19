package com.sun.movieapp.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.sun.movieapp.model.database.AppDatabase
import com.sun.movieapp.network.GenreService
import com.sun.movieapp.network.Network
import com.sun.movieapp.repository.GenreRepository
import com.sun.movieapp.ui.genre_selection.GenreSelectionViewModel
import com.sun.movieapp.ui.splash.SplashViewModel

class ViewModelFactory(private val activity: AppCompatActivity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(GenreSelectionViewModel::class.java) -> {
                val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "MovieApp").build()
                val repository = GenreRepository(Network.create(GenreService::class.java), db.genreDao())
                @Suppress("UNCHECKED_CAST")
                return GenreSelectionViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "MovieApp").build()
                val repository = GenreRepository(Network.create(GenreService::class.java), db.genreDao())
                @Suppress("UNCHECKED_CAST")
                return SplashViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
