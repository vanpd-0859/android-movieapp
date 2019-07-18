package com.sun.movieapp.ui.genre_selection

import androidx.lifecycle.MutableLiveData
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Genre

class GenreItemViewModel: BaseViewModel() {
    private val mGenreName: MutableLiveData<String> = MutableLiveData()

    fun bind(genre: Genre) {
        mGenreName.value = genre.name
    }

    fun getGenreName(): MutableLiveData<String>{
        return mGenreName
    }
}
