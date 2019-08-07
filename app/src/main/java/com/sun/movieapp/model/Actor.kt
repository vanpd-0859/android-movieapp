package com.sun.movieapp.model

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

data class Actor(
    @SerializedName("id")
    val personId: Int,
    var name: String = "",
    @SerializedName("profile_path")
    var profilePath: String = ""
) {
    companion object {
        val mDiffCalback = object: DiffUtil.ItemCallback<Actor>() {
            override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
                return oldItem.personId == newItem.personId
            }

            override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
                return oldItem.name == newItem.name &&
                        oldItem.profilePath == newItem.profilePath
            }
        }
    }
}
