package com.sun.movieapp.ui.genre_selection

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.repository.GenreRepository
import com.sun.movieapp.utils.extensions.async
import com.sun.movieapp.utils.extensions.loading

class GenreSelectionViewModel(
    private val mGenreRepository: GenreRepository
): BaseViewModel() {
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadGenres() }
    val genreListAdapter = GenreListAdapter()

    init {
        loadGenres()
        print("Hello")
    }

    private fun loadGenres() {
        rx {
            mGenreRepository.getGenres()
                .async()
                .loading(loading)
                .subscribe(
                    {
                        genreListAdapter.updatePostList(it)
                    },
                    {
                        error.value = it
                    }
                )
        }
    }
}
