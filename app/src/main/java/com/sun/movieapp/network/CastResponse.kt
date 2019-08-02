package com.sun.movieapp.network

import com.sun.movieapp.model.Actor

data class CastResponse(
    val cast: List<Actor>
)
