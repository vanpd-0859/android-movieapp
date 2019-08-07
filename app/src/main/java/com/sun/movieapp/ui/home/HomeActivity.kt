package com.sun.movieapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.sun.movieapp.R
import com.sun.movieapp.base.BaseActivity
import com.sun.movieapp.base.ViewModelFactory
import com.sun.movieapp.databinding.ActivityHomeBinding
import com.sun.movieapp.model.Movie
import com.sun.movieapp.ui.moviedetail.MovieDetailActivity
import com.sun.movieapp.ui.searchmovie.SearchMovieActivity
import com.sun.movieapp.utils.ExtraStrings
import com.sun.movieapp.utils.extensions.showError
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity: BaseActivity() {
    private lateinit var mBinding: ActivityHomeBinding
    private lateinit var mViewModel: HomeViewModel
    private var mIsLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        mViewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(HomeViewModel::class.java)

        val mPopularHelper = LinearSnapHelper()
        mPopularHelper.attachToRecyclerView(rvPopularMovie)
        val mUpcomingHelper = LinearSnapHelper()
        mUpcomingHelper.attachToRecyclerView(rvUpcomingMovie)

        val mOnClickItemListener: (Movie) -> Unit = {
            val intent = Intent(this@HomeActivity, MovieDetailActivity::class.java)
            intent.putExtra(ExtraStrings.MOVIE_EXTRA, it)
            startActivity(intent)
        }

        mViewModel.upcomingMovieAdapter = MovieListAdapter(mOnClickItemListener)
        mViewModel.popularMovieAdapter = MovieListAdapter(mOnClickItemListener)

        rvPopularMovie.adapter = mViewModel.popularMovieAdapter
        rvPopularMovie.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvUpcomingMovie.adapter = mViewModel.upcomingMovieAdapter
        rvUpcomingMovie.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvPopularMovie.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (!mIsLoading) {
                    //Log.d("SCROLL", "rvPopularMovie + ${layoutManager.findLastCompletelyVisibleItemPosition()}")
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == mViewModel.getPopularMovieSize()) {
                        mViewModel.loadMorePopularMovies()
                    }
                }
            }
        })

        rvUpcomingMovie.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (!mIsLoading) {
                    //Log.d("SCROLL", "rvUpcomingMovie + ${layoutManager.findLastCompletelyVisibleItemPosition()}")
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == mViewModel.getUpcommingMovieSize()) {
                        mViewModel.loadMoreUpcomingMovies()
                    }
                }
            }
        })

        mViewModel.loading.observe(this, Observer {
            mIsLoading = it
        })

        mViewModel.error.observe(this, Observer {
            val listener: (View) -> Unit = { mViewModel.loadData() }
            clHome.showError(it, Pair(R.string.retry, listener))
        })

        mViewModel.loadData()

        mBinding.viewModel = mViewModel
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.mnuSearch -> {
                Log.d("MENU", "SEARCH")
                val intent = Intent(this@HomeActivity, SearchMovieActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.mnuExpand -> {
                Log.d("MENU", "EXPAND")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
