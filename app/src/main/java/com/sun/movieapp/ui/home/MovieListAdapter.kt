package com.sun.movieapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sun.movieapp.R
import com.sun.movieapp.databinding.RowMovieItemBinding
import com.sun.movieapp.model.Movie

class MovieListAdapter(
    private val mOnItemClick: ((Movie) -> Unit)? = null
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mMovieList: List<Movie> = ArrayList()

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val mBinding: RowMovieItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_movie_item,
                parent,
                false
            )
            ItemViewHolder(mBinding)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_loading_vertical, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(mMovieList[position])
        } else if (holder is LoadingViewHolder) {
            holder.progressBar.isIndeterminate = true
        }
    }

    override fun getItemCount(): Int = mMovieList.size + 1

    fun updateMovieList(movieList: List<Movie>) {
        mMovieList = movieList
        notifyItemInserted(mMovieList.size - 1)
    }

    override fun getItemViewType(position: Int): Int = if (position >= mMovieList.size) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM

    inner class ItemViewHolder(private val mBinding: RowMovieItemBinding): RecyclerView.ViewHolder(mBinding.root) {
        private val mViewModel = MovieItemViewModel()

        fun bind(movie: Movie) {
            mViewModel.bind(movie)
            mViewModel.listener = View.OnClickListener { mOnItemClick?.invoke(mMovieList[adapterPosition]) }
            mBinding.viewModel = mViewModel
        }
    }

    inner class LoadingViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val progressBar: ProgressBar = view.findViewById(R.id.pbLoading)
    }
}
