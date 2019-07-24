package com.sun.movieapp.ui.splash

import android.os.Bundle
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sun.movieapp.base.BaseActivity
import com.sun.movieapp.base.ViewModelFactory
import com.sun.movieapp.ui.genreselection.GenreSelectionActivity
import com.sun.movieapp.ui.home.HomeActivity

class SplashActivity: BaseActivity() {
    private lateinit var mViewModel: SplashViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var intent: Intent?
        mViewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(SplashViewModel::class.java)
        mViewModel.favoriteGenres.observe(this, Observer {
            if (it.isNotEmpty()) {
                intent = Intent(this, HomeActivity::class.java)
            } else {
                intent = Intent(this, GenreSelectionActivity::class.java)
            }
            startActivity(intent)
            finish()
        })
    }
}
