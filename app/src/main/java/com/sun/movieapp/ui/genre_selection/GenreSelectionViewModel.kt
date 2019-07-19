package com.sun.movieapp.ui.genre_selection

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Genre
import com.sun.movieapp.model.GenreDao
import com.sun.movieapp.repository.GenreRepository
import com.sun.movieapp.utils.extensions.async
import com.sun.movieapp.utils.extensions.loading
import io.reactivex.subjects.BehaviorSubject

class GenreSelectionViewModel(
    private val mGenreRepository: GenreRepository
): BaseViewModel() {
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadGenres() }
    val genreListAdapter = GenreListAdapter()
    private val mSelectedGenres: BehaviorSubject<MutableList<Genre>> = BehaviorSubject.createDefault(ArrayList())
    private val _isDoneButtonEnabled: MutableLiveData<Boolean> = MutableLiveData()
    val isDoneButtonEnabled: LiveData<Boolean> = _isDoneButtonEnabled

    init {
        loadGenres()
        genreListAdapter.listener = object: GenreListAdapter.OnItemClickListener {
            override fun onItemClick(genre: Genre?) {
                genre?.let { item ->
                    mSelectedGenres.value?.let { selectedGenres ->
                        val genres = selectedGenres
                        if (selectedGenres.contains(item)) genres.remove(item) else genres.add(item)
                        mSelectedGenres.onNext(genres)
                    }
                }
            }
        }
        rx {
            mSelectedGenres.subscribe {
                _isDoneButtonEnabled.value = it.isNotEmpty()
            }
        }
    }

    fun saveSelectedGenres() {
        mSelectedGenres.value?.let {
            if (it.isNotEmpty())
                rx {
                    mGenreRepository.saveFavoriteGenres(it)
                        .async()
                        .subscribe({
                            Log.d("SAVE", "Success!")
                        }, {
                            Log.d("ERROR", it.localizedMessage)
                        })
                }
        }
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
