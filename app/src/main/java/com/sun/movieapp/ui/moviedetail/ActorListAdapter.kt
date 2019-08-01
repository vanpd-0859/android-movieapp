package com.sun.movieapp.ui.moviedetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sun.movieapp.R
import com.sun.movieapp.databinding.RowActorItemBinding
import com.sun.movieapp.model.Actor

class ActorListAdapter: ListAdapter<Actor, ActorListAdapter.ViewHolder>(mDiffCalback) {
    var listener: ActorListAdapter.OnItemClickListener? = null

    companion object {
        private val mDiffCalback = object: DiffUtil.ItemCallback<Actor>() {
            override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
                return oldItem.personId == newItem.personId
            }

            override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.profilePath == newItem.profilePath
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding: RowActorItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_actor_item,
            parent,
            false
        )
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val mBinding: RowActorItemBinding): RecyclerView.ViewHolder(mBinding.root) {
        private val mViewModel = ActorItemViewModel()

        fun bind(actor: Actor) {
            mViewModel.bind(actor)
            mViewModel.listener = object: View.OnClickListener {
                override fun onClick(view: View?) {
                    listener?.onItemClick(getItem(adapterPosition))
                }
            }
            mBinding.viewModel = mViewModel
        }
    }

    interface OnItemClickListener {
        fun onItemClick(actor: Actor)
    }
}
