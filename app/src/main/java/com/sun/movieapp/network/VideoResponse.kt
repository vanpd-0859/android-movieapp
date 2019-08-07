package com.sun.movieapp.network

import com.google.gson.annotations.SerializedName
import com.sun.movieapp.model.Video

data class VideoResponse(
    @SerializedName("results")
    var videos: List<Video>?
)
