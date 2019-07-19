package com.sun.movieapp.ui.genre_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sun.movieapp.R
import com.sun.movieapp.databinding.RowGenreItemBinding
import com.sun.movieapp.model.Genre

class GenreListAdapter: RecyclerView.Adapter<GenreListAdapter.ViewHolder>() {
    private lateinit var mGenreList: List<Genre>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding: RowGenreItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_genre_item,
            parent,
            false)
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(mGenreList[position])

    override fun getItemCount(): Int = if(::mGenreList.isInitialized) mGenreList.size else 0


    fun updatePostList(genreList: List<Genre>){
        this.mGenreList = genreList
        notifyDataSetChanged()
    }

    class ViewHolder(private val mBinding: RowGenreItemBinding):RecyclerView.ViewHolder(mBinding.root) {
        private val mViewModel = GenreItemViewModel()

        fun bind(genre: Genre){
            mViewModel.bind(genre)
            mBinding.viewModel = mViewModel
        }
    }
}
