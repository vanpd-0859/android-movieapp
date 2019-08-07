package com.sun.movieapp.ui.moviedetail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.movieapp.R
import com.sun.movieapp.base.BaseActivity
import com.sun.movieapp.base.ViewModelFactory
import com.sun.movieapp.databinding.ActivityMovieDetailBinding
import com.sun.movieapp.model.Movie
import com.sun.movieapp.utils.Constants
import com.sun.movieapp.utils.ExtraStrings
import com.sun.movieapp.utils.extensions.showError
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity: BaseActivity() {
    private lateinit var mViewModel: MovieDetailViewModel
    private lateinit var mBinding: ActivityMovieDetailBinding
    private lateinit var mMovie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
        mViewModel = ViewModelProviders.of(this, ViewModelFactory(this))
            .get(MovieDetailViewModel::class.java)
        mMovie = intent.getSerializableExtra(ExtraStrings.MOVIE_EXTRA) as Movie
        mViewModel.loadMovieDetail(mMovie)
        rvActor.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvActor.adapter = mViewModel.adapter
        wvTrailer.setBackgroundColor(Color.TRANSPARENT)
        wvTrailer.settings.javaScriptEnabled = true
        mViewModel.video.observe(this, Observer {
            it?.let {
                Log.d("LOAD_YT", Constants.YOUTUBE_EMBED + it.key)
                wvTrailer.loadUrl(Constants.YOUTUBE_EMBED + it.key)
            }
        })
        imbLike.setOnClickListener {
            mViewModel.likeButtonTapped()
            mViewModel.isLiked.value?.let {
                mViewModel.isLiked.value = !it
            }
        }
        mViewModel.error.observe(this, Observer {
            val listener: (View) -> Unit = { mViewModel.reload() }
            llMovieDetail.showError(it, Pair(R.string.retry, listener))
        })
        mBinding.viewModel = mViewModel
    }
}
