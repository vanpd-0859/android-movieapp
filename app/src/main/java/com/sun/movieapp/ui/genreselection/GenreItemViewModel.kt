package com.sun.movieapp.ui.genreselection

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Genre

class GenreItemViewModel: BaseViewModel() {
    private val mGenre: MutableLiveData<Genre> = MutableLiveData()
    var listener: View.OnClickListener? = null

    fun bind(genre: Genre) {
        mGenre.value = genre
    }

    fun getName(): LiveData<String> = Transformations.map(mGenre) { it.name }

    fun isSelected(): LiveData<Boolean> = Transformations.map(mGenre) { it.isSelected }

    fun onItemClick(view: View?) {
        listener?.onClick(view)
    }
}
