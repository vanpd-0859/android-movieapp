package com.sun.movieapp.ui.searchmovie

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Movie
import com.sun.movieapp.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class SearchMovieItemViewModel: BaseViewModel() {
    private val mMovie: MutableLiveData<Movie> = MutableLiveData()
    var listener: View.OnClickListener? = null

    fun bind(movie: Movie) {
        mMovie.value = movie
    }

    fun getTitle(): LiveData<String> = Transformations.map(mMovie, { it.title })
    fun getRating(): LiveData<String> = Transformations.map(mMovie, { "${it.voteAverage} / 10" })
    fun getReleaseDate(): LiveData<String> = Transformations.map(mMovie, {
        val format = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
        return@map format.format(it.releaseDate)
    })
    fun getPosterPath(): LiveData<String> = Transformations.map(mMovie, { it.posterPath })

    fun onItemClick(view: View?) {
        listener?.onClick(view)
    }
}
