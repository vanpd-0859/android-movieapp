package com.sun.movieapp.ui.genre_selection

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sun.movieapp.repository.GenreRepository

class GenreSelectionViewModelFactory(
    private val mGenreRepository: GenreRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GenreSelectionViewModel(mGenreRepository) as T
    }
}
