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
    private val _favoriteGenres: MutableLiveData<List<Genre>> = MutableLiveData()
    private val _error: MutableLiveData<Throwable> = MutableLiveData()
    val favoriteGenres: LiveData<List<Genre>> = _favoriteGenres
    val error: LiveData<Throwable> = _error

    init {
        rx {
            mGenreRepository.getFavoriteGenres()
                .async()
                .subscribe({
                    _favoriteGenres.value = it
                }, {
                    _error.value = it
                })
        }
    }
}
