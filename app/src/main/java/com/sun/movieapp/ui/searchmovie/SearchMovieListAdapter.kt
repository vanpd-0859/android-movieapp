package com.sun.movieapp.ui.searchmovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sun.movieapp.R
import com.sun.movieapp.databinding.RowMovieSearchItemBinding
import com.sun.movieapp.model.Movie

class SearchMovieListAdapter(
    private val mOnItemClick: ((Movie) -> Unit)? = null
): ListAdapter<Movie, SearchMovieListAdapter.ViewHolder>(mDiffCallback) {
    companion object {
        private val mDiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.voteAverage == newItem.voteAverage &&
                        oldItem.releaseDate == newItem.releaseDate &&
                        oldItem.posterPath == newItem.posterPath &&
                        oldItem.isLiked == newItem.isLiked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding: RowMovieSearchItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_movie_search_item,
            parent,
            false
        )
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val mBinding: RowMovieSearchItemBinding) : RecyclerView.ViewHolder(mBinding.root) {
        private val mViewModel = SearchMovieItemViewModel()

        fun bind(movie: Movie) {
            mViewModel.bind(movie)
            mViewModel.listener = object : View.OnClickListener {
                override fun onClick(view: View?) {
                    mOnItemClick?.invoke(getItem(adapterPosition))
                }
            }
            mBinding.viewModel = mViewModel
        }
    }
}
