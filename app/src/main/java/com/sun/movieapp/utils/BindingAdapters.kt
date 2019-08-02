package com.sun.movieapp.utils


import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.*
import com.bumptech.glide.request.transition.Transition
import com.sun.movieapp.R
import com.sun.movieapp.utils.Constants.ASSET_URL
import com.sun.movieapp.utils.extensions.getParentActivity

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: LiveData<Boolean>?) {
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
fun setBackground(layout: RelativeLayout, isSelected: LiveData<Boolean>?) {
    val mParentActivity: AppCompatActivity? = layout.getParentActivity()
    if (mParentActivity != null && isSelected != null) {
        isSelected.observe(mParentActivity, Observer {
                layout.setBackgroundResource(if (it) R.drawable.border_genre_row_selected else R.drawable.border_genre_row_unselected)
        })
    }
}

@BindingAdapter("mutableText")
fun setMutableText(textView: TextView, textContent: LiveData<String>?) {
    val mParentActivity: AppCompatActivity? = textView.getParentActivity()
    if (mParentActivity != null && textContent != null) {
        textContent.observe(mParentActivity, Observer {
                textView.text = it
        })
    }
}

@BindingAdapter("imageSrc")
fun setImageSrc(imageView: ImageView, imageSrc: LiveData<String>?) {
    val mParentActivity: AppCompatActivity? = imageView.getParentActivity()
    if (mParentActivity != null && imageSrc != null) {
        imageSrc.observe(mParentActivity, Observer {
                Glide.with(mParentActivity)
                    .load(ASSET_URL + it)
                    .placeholder(R.drawable.image_movie_placeholder)
                    .into(imageView)
        })
    }
}

@BindingAdapter("liked")
fun likeMovie(imageView: ImageView, isLiked: LiveData<Boolean>?) {
    val mParentActivity: AppCompatActivity? = imageView.getParentActivity()
    if (mParentActivity != null && isLiked != null) {
        isLiked.observe(mParentActivity, Observer {
            if (it) imageView.setImageResource(R.drawable.ic_like) else imageView.setImageResource(R.drawable.ic_unlike)
        })
    }
}

@BindingAdapter("setBackground")
fun setBackground(linerLayout: LinearLayout, path: LiveData<String>?) {
    val mParentActivity: AppCompatActivity? = linerLayout.getParentActivity()
    if (mParentActivity != null && path != null) {
        path.observe(mParentActivity, Observer {
            Glide.with(mParentActivity)
                .asDrawable()
                .load(ASSET_URL + it)
                .into(object: CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        linerLayout.background = resource
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        })
    }
}
