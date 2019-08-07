package com.sun.movieapp.ui.genreselection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sun.movieapp.R
import com.sun.movieapp.databinding.RowGenreItemBinding
import com.sun.movieapp.model.Genre

class GenreListAdapter(
    private val mOnItemClick: ((Genre) -> Unit)? = null
): ListAdapter<Genre, GenreListAdapter.ViewHolder>(Genre.mDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding: RowGenreItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_genre_item,
            parent,
            false)
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val mBinding: RowGenreItemBinding):RecyclerView.ViewHolder(mBinding.root) {
        private val mViewModel = GenreItemViewModel()

        fun bind(genre: Genre){
            mViewModel.bind(genre)
            mViewModel.listener = View.OnClickListener {
                val currentItem = getItem(adapterPosition)
                mOnItemClick?.invoke(currentItem)
                currentItem.isSelected = !currentItem.isSelected
                notifyItemChanged(adapterPosition)
            }
            mBinding.viewModel = mViewModel
        }
    }
}
