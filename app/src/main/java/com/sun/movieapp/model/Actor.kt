package com.sun.movieapp.model

import com.google.gson.annotations.SerializedName

data class Actor(
    @SerializedName("id")
    val personId: Int,
    val name: String,
    @SerializedName("profile_path")
    val profilePath: String
)
