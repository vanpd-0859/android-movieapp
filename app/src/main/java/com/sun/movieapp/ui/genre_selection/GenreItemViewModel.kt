package com.sun.movieapp.ui.genre_selection

import androidx.lifecycle.MutableLiveData
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Genre

class GenreItemViewModel: BaseViewModel() {
    private val mGenre: MutableLiveData<Genre> = MutableLiveData()
    private val mIsSelected: MutableLiveData<Boolean> = MutableLiveData()
    var listener: GenreListAdapter.OnItemClickListener? = null

    fun bind(genre: Genre) {
        mGenre.value = genre
        mIsSelected.value = false
    }

    fun getGenreLiveData(): MutableLiveData<Genre> {
        return mGenre
    }

    fun getGenre(): Genre? {
        return mGenre.value
    }

    fun getIsSelected(): MutableLiveData<Boolean> {
        return mIsSelected
    }

    fun onItemClick(genre: Genre?) {
        listener?.onItemClick(genre)
        mIsSelected.value?.let {
            mIsSelected.value = !it
        }
    }
}
