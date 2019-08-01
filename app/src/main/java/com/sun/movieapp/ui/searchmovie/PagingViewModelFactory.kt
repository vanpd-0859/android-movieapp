package com.sun.movieapp.ui.searchmovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.subjects.BehaviorSubject

class PagingViewModelFactory(val currentPage: BehaviorSubject<Int>): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(PagingViewModel::class.java) -> {
                PagingViewModel(currentPage)
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }
}
