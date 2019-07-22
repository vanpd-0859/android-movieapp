package com.sun.movieapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Genre
import com.sun.movieapp.repository.GenreRepository
import com.sun.movieapp.utils.extensions.async

class SplashViewModel(
    private val mGenreRepository: GenreRepository
): BaseViewModel() {
    val favoriteGenres: MutableLiveData<List<Genre>> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()

    init {
        rx {
            mGenreRepository.getFavoriteGenres()
                .async()
                .subscribe({
                    favoriteGenres.value = it
                }, {
                    error.value = it
                })
        }
    }
}
