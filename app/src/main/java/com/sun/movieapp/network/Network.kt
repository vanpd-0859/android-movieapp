package com.sun.movieapp.network

import com.sun.movieapp.utils.BASE_URL
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object Network {
    @Reusable
    @JvmStatic
    private var mRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()

    @Reusable
    @JvmStatic
    fun <T>create(service: Class<T>): T {
        return mRetrofit.create(service)
    }
}
