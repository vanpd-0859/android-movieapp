package com.sun.movieapp.ui.moviedetail

import android.os.Bundle
import com.sun.movieapp.R
import com.sun.movieapp.base.BaseActivity

class MovieDetailActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
    }
}
