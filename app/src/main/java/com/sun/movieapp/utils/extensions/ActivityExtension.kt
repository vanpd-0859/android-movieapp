package com.sun.movieapp.utils.extensions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.dismissKeyboard() {
    val inputMethodManager = getSystemService( Context.INPUT_METHOD_SERVICE ) as InputMethodManager
    if( inputMethodManager.isAcceptingText )
        inputMethodManager.hideSoftInputFromWindow( this.currentFocus?.windowToken, 0)
}

fun AppCompatActivity.addFragment(fragment: Fragment, containerResId: Int) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.add(containerResId, fragment)
    transaction.commit()
}

fun AppCompatActivity.showFragment(fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.show(fragment)
    transaction.commit()
}

fun AppCompatActivity.hideFragment(fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.hide(fragment)
    transaction.commit()
}
