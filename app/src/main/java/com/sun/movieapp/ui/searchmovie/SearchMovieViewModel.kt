package com.sun.movieapp.ui.searchmovie

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.sun.movieapp.base.BaseViewModel
import com.sun.movieapp.model.Movie
import com.sun.movieapp.network.MovieResponse
import com.sun.movieapp.repository.GenreRepository
import com.sun.movieapp.repository.MovieRepository
import com.sun.movieapp.utils.extensions.async
import com.sun.movieapp.utils.extensions.loading
import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class SearchMovieViewModel(
    private val mGenreRepository: GenreRepository,
    private val mMovieRepository: MovieRepository
): BaseViewModel() {
    private val mMovies: BehaviorSubject<List<Movie>> = BehaviorSubject.createDefault(ArrayList())
    private var mIsSearching: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    val totalPages: MutableLiveData<Int> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()
    val errorClickListener = View.OnClickListener { loadData() }
    var genreAdapter: SearchGenreListAdapter? = null
    var movieAdapter: SearchMovieListAdapter? = null
    private var mQuery: String = ""
    private var mGenreIdList: MutableList<Int> = ArrayList()

    init {
        loadData()
    }

    fun loadData() {
        rx {
            mGenreRepository.getGenres()
                .async()
                .loading(loading)
                .subscribe({
                    genreAdapter?.submitList(it)
                }, {
                    error.value = it
                })
        }
        rx {
            mMovies.async()
                .skip(1)
                .subscribe {
                    movieAdapter?.submitList(it)
                    movieAdapter?.notifyDataSetChanged()
                }
        }
    }

    fun searchMovie(queryObservable: Observable<String>) {
        rx {
            queryObservable
                .debounce(300, TimeUnit.MILLISECONDS)
                .async()
                .filter {
                    if (it.isEmpty()) {
                        mMovies.onNext(ArrayList())
                        totalPages.value = 0
                        return@filter false
                    } else {
                        return@filter true
                    }
                }
                .distinctUntilChanged()
                .flatMap {
                    mQuery = it
                    mMovieRepository.searchMovie(it)
                        .async()
                        .loading(loading)
                        .toObservable()
                        .materialize()
                }
                .subscribe({ notification: Notification<MovieResponse> ->
                    if (notification.isOnNext) {
                        notification.value?.let {
                            mIsSearching.onNext(true)
                            mMovies.onNext(it.movies)
                            totalPages.value = if (it.totalPages > 999) 999 else it.totalPages
                        }
                    }
                }, {
                    error.value = it
                })
        }
    }

    fun searchMovieByGenre(genreIdListObservable: Observable<MutableList<Int>>) {
        rx {
            genreIdListObservable
                .async()
                .filter {
                    if (it.isEmpty()) {
                        mMovies.onNext(ArrayList())
                        totalPages.value = 0
                        return@filter false
                    } else {
                        return@filter true
                    }
                }
                .flatMap {
                    mGenreIdList = it
                    mMovieRepository.searchMovieByGenre(it)
                        .async()
                        .loading(loading)
                        .toObservable()
                        .materialize()
                }
                .subscribe({ notification: Notification<MovieResponse> ->
                    if (notification.isOnNext) {
                        notification.value?.let {
                            mIsSearching.onNext(false)
                            mMovies.onNext(it.movies)
                            totalPages.value = if (it.totalPages > 999) 999 else it.totalPages
                        }
                    }
                }, {
                    error.value = it
                })
        }
    }

    fun paging(pageObservable: Observable<Int>) {
        rx {
            pageObservable
                .async()
                .withLatestFrom(mIsSearching, BiFunction { page: Int, isSearching: Boolean ->
                Pair(page, isSearching)
                })
                .skip(1)
                .flatMap {
                    val (page, isSeaching) = it
                    if (isSeaching) {
                        mMovieRepository.searchMovie(mQuery, page)
                            .async()
                            .loading(loading)
                            .toObservable()
                            .materialize()
                    } else {
                        mMovieRepository.searchMovieByGenre(mGenreIdList, page)
                            .async()
                            .loading(loading)
                            .toObservable()
                            .materialize()
                    }
                }
                .subscribe({ notification ->
                    if (notification.isOnNext) {
                        notification.value?.let {
                            mMovies.onNext(it.movies)
                        }
                    }
                }, {
                    error.value = it
                })
        }
    }
}
