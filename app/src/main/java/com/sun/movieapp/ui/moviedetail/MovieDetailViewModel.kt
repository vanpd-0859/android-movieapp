package com.sun.movieapp.ui.moviedetail

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Actor
import com.sun.movieapp.model.Movie
import com.sun.movieapp.model.MovieDetail
import com.sun.movieapp.model.Video
import com.sun.movieapp.repository.MovieRepository
import com.sun.movieapp.utils.extensions.async
import com.sun.movieapp.utils.extensions.catchNullException
import com.sun.movieapp.utils.extensions.loading

class MovieDetailViewModel(
    private val mMovieRepository: MovieRepository
): BaseViewModel() {
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()
    val errorClickListener: (View) -> Unit = { loadMovieDetail(mMovie) }
    val adapter = ActorListAdapter()
    val video: MutableLiveData<Video> = MutableLiveData()
    val isLiked: MutableLiveData<Boolean> = MutableLiveData()
    private val mMovieDetail: MutableLiveData<MovieDetail> = MutableLiveData()
    private lateinit var mMovie: Movie

    init {
        isLiked.value = false
    }

    fun loadMovieDetail(movie: Movie) {
        mMovie = movie
        rx {
            mMovieRepository.getMovieDetail(movie.id)
                .async()
                .loading(loading)
                .subscribe({
                    mMovieDetail.value = it
                    Log.i("POSTER_PATH", it.posterPath)
                }, {
                    error.value = it
                })
        }
        rx {
            mMovieRepository.getCast(movie.id)
                .async()
                .loading(loading)
                .subscribe({
                    adapter.submitList(it)
                }, {
                    error.value = it
                })
        }
        rx {
            mMovieRepository.getVideoOrNull(movie.id)
                .async()
                .loading(loading)
                .catchNullException()
                .subscribe({
                    it?.let {
                        video.value = it
                    }
                }, {
                    error.value = it
                })
        }
        rx {
            mMovieRepository.getSavedMovies()
                .map { movieList ->
                    val movie = movieList.firstOrNull { it.isLiked && it.id == movie.id }
                    movie != null
                }
                .async()
                .subscribe({
                    isLiked.value = it
                }, {
                    error.value = it
                })
        }
    }

    fun likeButtonTapped() {
        rx {
            mMovieRepository.getSavedMovies()
                .async()
                .subscribe({ movies ->
                    var movie = movies.firstOrNull { it.id == mMovie.id }
                    if (movie != null) {
                        movie.isLiked = !movie.isLiked
                        rx {
                            mMovieRepository.updateMovie(movie)
                                .async()
                                .subscribe({}, { Log.e("ERROR", it.localizedMessage) })
                        }
                    } else {
                        mMovie.isLiked = true
                        rx {
                            mMovieRepository.insertMovie(mMovie)
                                .async()
                                .subscribe({}, { Log.e("ERROR", it.localizedMessage) })
                        }
                    }
                }, {
                    error.value = it
                })
        }
    }

    fun getTitle(): LiveData<String> = Transformations.map(mMovieDetail) { it.title }
    fun getRating(): LiveData<String> = Transformations.map(mMovieDetail) { "${it.voteAverage} / 10" }
    fun getOverview(): LiveData<String> = Transformations.map(mMovieDetail) { it.overview }
    fun getReleaseDate(): LiveData<String> = Transformations.map(mMovieDetail) { if (it.releaseDate.isEmpty()) "0000-00-00" else it.releaseDate }
    fun getPosterPath(): LiveData<String> = Transformations.map(mMovieDetail) { it.posterPath }
    fun isLiked(): LiveData<Boolean> = isLiked
}
