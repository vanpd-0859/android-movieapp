package com.sun.movieapp.ui.searchmovie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.movieapp.R
import com.sun.movieapp.base.BaseActivity
import com.sun.movieapp.base.ViewModelFactory
import com.sun.movieapp.databinding.ActivitySearchMovieBinding
import com.sun.movieapp.ui.moviedetail.MovieDetailActivity
import com.sun.movieapp.utils.ExtraStrings
import com.sun.movieapp.utils.extensions.showError
import kotlinx.android.synthetic.main.activity_search_movie.*
import androidx.recyclerview.widget.RecyclerView
import com.sun.movieapp.utils.RxSearchObservable
import com.sun.movieapp.utils.extensions.dismissKeyboard
import io.reactivex.subjects.BehaviorSubject

class SearchMovieActivity: BaseActivity() {
    private lateinit var mViewModel: SearchMovieViewModel
    private lateinit var mBinding: ActivitySearchMovieBinding
    private val mGenreIdList: BehaviorSubject<MutableList<Int>> = BehaviorSubject.createDefault(ArrayList())

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this@SearchMovieActivity, R.layout.activity_search_movie)
        mViewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(SearchMovieViewModel::class.java)
        mViewModel.movieAdapter = SearchMovieListAdapter {
            val intent = Intent(this@SearchMovieActivity, MovieDetailActivity::class.java)
            intent.putExtra(ExtraStrings.MOVIE_EXTRA, it)
            startActivity(intent)
        }
        mViewModel.genreAdapter = SearchGenreListAdapter { genre ->
            mGenreIdList.value?.let {
                val currentList= it
                if (currentList.contains(genre.id)) currentList.remove(genre.id) else currentList.add(genre.id)
                mGenreIdList.onNext(currentList)
            }
        }
        rvGenre.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvGenre.adapter = mViewModel.genreAdapter
        rvSearchMovie.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvSearchMovie.adapter = mViewModel.movieAdapter
        rvSearchMovie.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) dismissKeyboard()
            }
        })

        mViewModel.error.observe(this, Observer {
            clSearchMovie.showError(it, Pair(R.string.retry, mViewModel.errorClickListener))
        })

        mViewModel.searchMovieByGenre(mGenreIdList)
        mBinding.viewModel = mViewModel
        val fragment = PagingFragment()
        mViewModel.paging(fragment.currentPage)
        addFragment(fragment)
        hideFragment(fragment)
        mViewModel.totalPages.observe(this, Observer {
            if (it > 0) {
                showFragment(fragment)
                fragment.updateTotalPages(it)
            } else {
                hideFragment(fragment)
            }
        })
    }

    private fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.llContainer, fragment)
        transaction.commit()
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.show(fragment)
        transaction.commit()
    }

    private fun hideFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_searchmovie, menu)
        menu?.let {
            val mSearchView = it.findItem(R.id.searchBar).actionView as SearchView
            mSearchView.queryHint = resources.getString(R.string.search_movie)
            mSearchView.isIconified = false
            mViewModel.searchMovie(RxSearchObservable.fromView(mSearchView))
        }
        return true
    }
}
