package com.sun.movieapp.ui.home

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Movie

class MovieItemViewModel: BaseViewModel() {
    private val mMovie: MutableLiveData<Movie> = MutableLiveData()
    var listener: View.OnClickListener? = null

    fun bind(movie: Movie) {
        mMovie.value = movie
    }

    fun getTitle(): LiveData<String> = Transformations.map(mMovie, { it.title })

    fun getRating(): LiveData<String> = Transformations.map(mMovie, { "${it.voteAverage} / 10" })

    fun getPosterPath(): LiveData<String> = Transformations.map(mMovie, { it.posterPath })

    fun onItemClick(view: View?) {
        listener?.onClick(view)
    }
}
