package com.sun.movieapp.utils

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.sun.movieapp.R
import com.sun.movieapp.model.Genre
import com.sun.movieapp.utils.extensions.getParentActivity

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Boolean>?) {
    val mParentActivity: AppCompatActivity? = view.getParentActivity()
    if (mParentActivity != null && visibility != null) {
        visibility.observe(mParentActivity, Observer {
                value -> view.visibility = if (value) View.VISIBLE else View.GONE
        })
    }
}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("toggleBackground")
fun setBackground(layout: RelativeLayout, genre: MutableLiveData<Genre>?) {
    val mParentActivity: AppCompatActivity? = layout.getParentActivity()
    if (mParentActivity != null && genre != null) {
        genre.observe(mParentActivity, Observer {
                item -> layout.setBackgroundResource(if (item.isSelected) R.drawable.border_genre_row_selected else R.drawable.border_genre_row_unselected)
        })
    }
}

@BindingAdapter("itemObject")
fun setGenreName(textView: TextView, genre: MutableLiveData<Genre>?) {
    val mParentActivity: AppCompatActivity? = textView.getParentActivity()
    if (mParentActivity != null && genre != null) {
        genre.observe(mParentActivity, Observer {
                item -> textView.text = item.name
        })
    }
}
