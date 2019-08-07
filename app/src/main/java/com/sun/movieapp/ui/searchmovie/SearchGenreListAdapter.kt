package com.sun.movieapp.ui.searchmovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sun.movieapp.R
import com.sun.movieapp.databinding.RowGenreSearchItemBinding
import com.sun.movieapp.model.Genre
import com.sun.movieapp.ui.genreselection.GenreItemViewModel

class SearchGenreListAdapter(
    private val mOnItemClick: ((Genre) -> Unit)? = null
): ListAdapter<Genre, SearchGenreListAdapter.ViewHolder>(Genre.mDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding: RowGenreSearchItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_genre_search_item,
            parent,
            false)
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val mBinding: RowGenreSearchItemBinding): RecyclerView.ViewHolder(mBinding.root) {
        private val mViewModel = GenreItemViewModel()

        fun bind(genre: Genre){
            mViewModel.bind(genre)
            mViewModel.listener = object: View.OnClickListener {
                override fun onClick(view: View?) {
                    val currentItem = getItem(adapterPosition)
                    mOnItemClick?.invoke(currentItem)
                    currentItem.isSelected = !currentItem.isSelected
                    notifyItemChanged(adapterPosition)
                }
            }
            mBinding.viewModel = mViewModel
        }
    }
}
