package com.sun.movieapp.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.movieapp.ui.genreselection.GenreSelectionViewModel
import com.sun.movieapp.ui.home.HomeViewModel
import com.sun.movieapp.ui.moviedetail.MovieDetailViewModel
import com.sun.movieapp.ui.splash.SplashViewModel
import kotlinx.android.synthetic.main.activity_home.*

class ViewModelFactory(private val activity: AppCompatActivity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(GenreSelectionViewModel::class.java) -> {
                GenreSelectionViewModel(BaseRepository.getGenreRepository(activity))
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(BaseRepository.getGenreRepository(activity))
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(BaseRepository.getMovieRepository(activity))
            }
            modelClass.isAssignableFrom(MovieDetailViewModel::class.java) -> {
                MovieDetailViewModel(BaseRepository.getMovieRepository(activity))
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }
}
