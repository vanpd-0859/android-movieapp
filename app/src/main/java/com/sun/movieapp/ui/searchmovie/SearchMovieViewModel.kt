package com.sun.movieapp.ui.searchmovie

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Movie
import com.sun.movieapp.repository.GenreRepository
import com.sun.movieapp.repository.MovieRepository
import com.sun.movieapp.utils.extensions.async
import com.sun.movieapp.utils.extensions.loading
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class SearchMovieViewModel(
    private val mGenreRepository: GenreRepository,
    private val mMovieRepository: MovieRepository
): BaseViewModel() {
    private val mGenreIdList: BehaviorSubject<MutableList<Int>> = BehaviorSubject.createDefault(ArrayList())
    private val mMovies: BehaviorSubject<List<Movie>> = BehaviorSubject.createDefault(ArrayList())
    private var mIsSearching = false
    private var mTotalPages = 1
    private var mCurrentPage = 1
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val loadingLoadMore: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()
    val errorClickListener: (View) -> Unit = { loadData() }
    var genreAdapter = SearchGenreListAdapter { genre ->
        mGenreIdList.value?.let {
            val currentList= it
            if (currentList.contains(genre.id)) currentList.remove(genre.id) else currentList.add(genre.id)
            mGenreIdList.onNext(currentList)
        }
    }
    var movieAdapter: SearchMovieListAdapter? = null
    private var mQuery: String = ""
    private var mCurrentGenreIdList: List<Int> = ArrayList()

    init {
        loadData()
    }

    private fun loadData() {
        rx {
            mGenreRepository.getGenres()
                .async()
                .loading(loading)
                .subscribe({
                    genreAdapter.submitList(it)
                }, {
                    error.value = it
                })
        }
        rx {
            mMovies.async()
                .skip(1)
                .async()
                .subscribe {
                    movieAdapter?.updateMovieList(it)
                }
        }
        rx {
            mGenreIdList
                .skip(1)
                .async()
                .filter {
                    if (it.isEmpty()) resetData()
                    it.isNotEmpty()
                }
                .flatMap {
                    mCurrentGenreIdList = it
                    mMovieRepository.searchMovieByGenre(it)
                        .async()
                        .loading(loading)
                        .toObservable()
                }
                .subscribe({
                    mIsSearching = false
                    mMovies.onNext(it.movies)
                    mTotalPages = if (it.totalPages > 999) 999 else it.totalPages
                    mCurrentPage = 1
                }, {
                    error.value = it
                })
        }
    }

    fun searchMovie(queryObservable: Observable<String>) {
        rx {
            queryObservable
                .debounce(300, TimeUnit.MILLISECONDS)
                .async()
                .filter {
                    if (it.isEmpty()) resetData()
                    it.isNotEmpty()
                }
                .distinctUntilChanged()
                .doOnNext {
                    Log.d("SEARCH", it)
                }
                .flatMap {
                    mQuery = it
                    mMovieRepository.searchMovie(it)
                        .async()
                        .loading(loading)
                        .toObservable()
                }
                .subscribe({
                    mIsSearching = true
                    mMovies.onNext(it.movies)
                    mTotalPages = if (it.totalPages > 999) 999 else it.totalPages
                    mCurrentPage = 1
                }, {
                    error.value = it
                })
        }
    }

    fun loadMore() {
        mCurrentPage++
        rx {
            if (mIsSearching) {
                mMovieRepository.searchMovie(mQuery, mCurrentPage)
            } else {
                mMovieRepository.searchMovieByGenre(mCurrentGenreIdList, mCurrentPage)
            }
                .async()
                .loading(loadingLoadMore)
                .subscribe({ res ->
                    mMovies.value?.let {
                        val newList: MutableList<Movie> = ArrayList()
                        newList.addAll(it + res.movies)
                        mMovies.onNext(newList)
                    }
                }, {
                    error.value = it
                })
        }
    }

    fun getMovieListSize(): Int {
        return mMovies.value?.size ?: 0
    }

    private fun resetData() {
        mMovies.onNext(ArrayList())
    }
}
