package com.sun.movieapp.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.movieapp.ui.genreselection.GenreSelectionViewModel
import com.sun.movieapp.ui.home.HomeViewModel
import com.sun.movieapp.ui.splash.SplashViewModel
import kotlinx.android.synthetic.main.activity_home.*

class ViewModelFactory(private val activity: AppCompatActivity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(GenreSelectionViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return GenreSelectionViewModel(BaseRepository.getGenreRepository(activity)) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return SplashViewModel(BaseRepository.getGenreRepository(activity)) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(BaseRepository.getMovieRepository(activity)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
