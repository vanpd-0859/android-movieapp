package com.sun.movieapp.ui.genreselection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sun.movieapp.R
import com.sun.movieapp.databinding.RowGenreItemBinding
import com.sun.movieapp.model.Genre

class GenreListAdapter: ListAdapter<Genre, GenreListAdapter.ViewHolder>(mDiffCallback) {
    var listener: OnItemClickListener? = null
    companion object {
        private val mDiffCallback = object: DiffUtil.ItemCallback<Genre>() {
            override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

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
            mViewModel.listener = object: View.OnClickListener {
                override fun onClick(view: View?) {
                    val currentItem = getItem(adapterPosition)
                    listener?.onItemClick(currentItem)
                    currentItem.isSelected = !currentItem.isSelected
                    notifyItemChanged(adapterPosition)
                }
            }
            mBinding.viewModel = mViewModel
        }
    }

    interface OnItemClickListener {
        fun onItemClick(genre: Genre)
    }
}
