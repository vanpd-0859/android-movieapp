package com.sun.movieapp.ui.genre_selection

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Genre

class GenreItemViewModel: BaseViewModel() {
    private val mGenre: MutableLiveData<Genre> = MutableLiveData()
    var listener: View.OnClickListener? = null

    fun bind(genre: Genre) {
        mGenre.value = genre
    }

    fun getGenre(): MutableLiveData<Genre> {
        return mGenre
    }

    fun onItemClick(view: View?) {
        listener?.onClick(view)
    }
}
