package com.sun.movieapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sun.movieapp.R
import com.sun.movieapp.base.BaseActivity
import com.sun.movieapp.base.ViewModelFactory
import com.sun.movieapp.databinding.ActivityHomeBinding
import com.sun.movieapp.model.Movie
import com.sun.movieapp.ui.moviedetail.MovieDetailActivity
import com.sun.movieapp.utils.extensions.showError
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity: BaseActivity() {
    private lateinit var mBinding: ActivityHomeBinding
    private lateinit var mViewModel: HomeViewModel
    private var isLoading = false

    companion object {
        const val MOVIE_EXTRA = "com.sun.movieapp.ui.home.MOVIE_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        mViewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(HomeViewModel::class.java)

        rvPopularMovie.adapter = mViewModel.popularMovieAdapter
        rvPopularMovie.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvUpcomingMovie.adapter = mViewModel.upcomingMovieAdapter
        rvUpcomingMovie.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvPopularMovie.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (!isLoading) {
                    Log.d("SCROLL", "rvPopularMovie + ${layoutManager.findLastCompletelyVisibleItemPosition()}")
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == mViewModel.getPopularMovieSize()) {
                        mViewModel.loadMorePopularMovies()
                        isLoading = true
                    }
                }
            }
        })

        rvUpcomingMovie.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (!isLoading) {
                    Log.d("SCROLL", "rvUpcomingMovie + ${layoutManager.findLastCompletelyVisibleItemPosition()}")
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == mViewModel.getUpcommingMovieSize()) {
                        mViewModel.loadMoreUpcomingMovies()
                    }
                }
            }
        })

        mViewModel.loading.observe(this, Observer {
            isLoading = it
        })

        mViewModel.error.observe(this, Observer {
            clHome.showError(it, Pair(R.string.retry, mViewModel.errorClickListener))
        })

        val mOnClickItemListener = object: MovieListAdapter.OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                val intent = Intent(this@HomeActivity, MovieDetailActivity::class.java)
                intent.putExtra(MOVIE_EXTRA, movie)
                startActivity(intent)
            }
        }
        mViewModel.upcomingMovieAdapter.listener = mOnClickItemListener
        mViewModel.popularMovieAdapter.listener = mOnClickItemListener

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
