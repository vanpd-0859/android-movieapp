package com.sun.movieapp.ui.genreselection

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Genre
import com.sun.movieapp.repository.GenreRepository
import com.sun.movieapp.utils.extensions.async
import com.sun.movieapp.utils.extensions.loading
import io.reactivex.subjects.BehaviorSubject

class GenreSelectionViewModel(
    private val mGenreRepository: GenreRepository
): BaseViewModel() {
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()
    val errorClickListener: (View) -> Unit = { loadGenres() }
    val genreListAdapter = GenreListAdapter {
        mSelectedGenres.value?.let { selectedGenres ->
            val genres = selectedGenres
            if (selectedGenres.contains(it)) genres.remove(it) else genres.add(it)
            mSelectedGenres.onNext(genres)
        }
    }
    private val mSelectedGenres: BehaviorSubject<MutableList<Genre>> = BehaviorSubject.createDefault(ArrayList())
    val isDoneButtonEnabled: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loadGenres()
        rx {
            mSelectedGenres.subscribe {
                isDoneButtonEnabled.value = it.isNotEmpty()
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
                        genreListAdapter.submitList(it)
                    },
                    {
                        error.value = it
                    }
                )
        }
    }
}
