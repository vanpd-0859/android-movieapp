package com.sun.movieapp.base

import androidx.lifecycle.ViewModel
import com.sun.movieapp.network.GenreService
import com.sun.movieapp.network.Network
import com.sun.movieapp.repository.GenreRepository
import com.sun.movieapp.ui.genre_selection.GenreSelectionViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel: ViewModel() {
    protected var mCompositeDisposable: CompositeDisposable? = null

    init {
        mCompositeDisposable = CompositeDisposable()
    }

    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable?.dispose()
    }

    protected fun rx(block: () -> Disposable?) {
        if (mCompositeDisposable == null) throw IllegalArgumentException(
            "No worry! You've just forgot inject disposable, DO IT!")
        block()?.let { mCompositeDisposable?.add(it) }
    }
}
