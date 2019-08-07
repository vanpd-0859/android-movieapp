package com.sun.movieapp.ui.genreselection

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.sun.movieapp.R
import com.sun.movieapp.base.BaseActivity
import com.sun.movieapp.databinding.ActivityGenreSelectionBinding
import android.os.Build
import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.sun.movieapp.base.ViewModelFactory
import com.sun.movieapp.ui.home.HomeActivity
import com.sun.movieapp.utils.extensions.showError
import kotlinx.android.synthetic.main.activity_genre_selection.*

class GenreSelectionActivity: BaseActivity() {
    private lateinit var mBinding: ActivityGenreSelectionBinding
    private lateinit var mViewModel: GenreSelectionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        // This work only for android 4.4+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            window.decorView.systemUiVisibility = flags

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            val decorView = window.decorView
            decorView
                .setOnSystemUiVisibilityChangeListener { visibility ->
                    if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                        decorView.systemUiVisibility = flags
                    }
                }
        }

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_genre_selection)
        rvGenre.layoutManager = GridLayoutManager(this, 2)

        mViewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(GenreSelectionViewModel::class.java)
        mViewModel.error.observe(this, Observer {
            val listener: (View) -> Unit = { mViewModel.loadGenres() }
            clGenreSelection.showError(it, Pair(R.string.retry, listener))
        })
        mBinding.viewModel = mViewModel
        mViewModel.isDoneButtonEnabled.observe(this, Observer {
            btnDone.isEnabled = it
        })
        btnDone.setOnClickListener {
            mViewModel.saveSelectedGenres()
            val intent = Intent(this@GenreSelectionActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("NewApi")
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}
