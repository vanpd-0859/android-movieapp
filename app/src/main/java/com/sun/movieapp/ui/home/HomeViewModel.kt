package com.sun.movieapp.ui.home

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Movie
import com.sun.movieapp.repository.MovieRepository
import com.sun.movieapp.utils.extensions.async
import com.sun.movieapp.utils.extensions.loading
import io.reactivex.subjects.BehaviorSubject

class HomeViewModel(
    private val mMovieRepository: MovieRepository
): BaseViewModel() {
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()
    var upcomingMovieAdapter: MovieListAdapter? = null
    var popularMovieAdapter: MovieListAdapter? = null
    private val mUpcomingMovies: BehaviorSubject<MutableList<Movie>> = BehaviorSubject.createDefault(ArrayList())
    private val mPopularMovies: BehaviorSubject<MutableList<Movie>> = BehaviorSubject.createDefault(ArrayList())
    private var mUpcomingPages = 1
    private var mPopularPages = 1

    init {
        setData()
    }

    fun loadData() {
        loadPopularMovies()
        loadUpcomingMovies()
    }

    fun getUpcommingMovieSize(): Int {
        return mUpcomingMovies.value?.size ?: 0
    }

    fun getPopularMovieSize(): Int {
        return mPopularMovies.value?.size ?: 0
    }

    fun loadMoreUpcomingMovies() {
        mUpcomingPages++
        loadUpcomingMovies(mUpcomingPages)
    }

    fun loadMorePopularMovies() {
        mPopularPages++
        loadPopularMovies(mPopularPages)
    }

    private fun loadUpcomingMovies(page: Int = 1) {
        rx {
            mMovieRepository.getUpcomingMovies(page)
                .async()
                .loading(loading)
                .subscribe({ loaded ->
                    val mCurrent = mUpcomingMovies.value
                    mCurrent?.let {
                        mCurrent.addAll(loaded)
                        mUpcomingMovies.onNext(mCurrent)
                    }
                }, {
                    error.value = it
                })
        }
    }

    private fun loadPopularMovies(page: Int = 1) {
        rx {
            mMovieRepository.getPopularMovies(page)
                .async()
                .loading(loading)
                .subscribe({ loaded ->
                    val mCurrent = mPopularMovies.value
                    mCurrent?.let {
                        mCurrent.addAll(loaded)
                        mPopularMovies.onNext(mCurrent)
                    }
                }, {
                    error.value = it
                })
        }
    }

    private fun setData() {
        rx {
            mUpcomingMovies
                .async()
                .skip(1)
                .subscribe{
                    upcomingMovieAdapter?.updateMovieList(it)
                    //Log.e("UPCOMING", it.toString())
                }
        }

        rx {
            mPopularMovies
                .async()
                .skip(1)
                .subscribe {
                    popularMovieAdapter?.updateMovieList(it)
                    //Log.e("POPULAR", it.toString())
                }
        }
    }
}
