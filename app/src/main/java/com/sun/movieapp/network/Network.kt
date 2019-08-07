package com.sun.movieapp.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sun.movieapp.utils.ApiKeys.THE_MOVIE_DB_API_KEY
import com.sun.movieapp.utils.Constants.BASE_URL
import com.sun.movieapp.utils.Constants.DATE_FORMAT
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Logger

object Network {
    @JvmStatic
    private val mHttpClientBuilder = OkHttpClient.Builder();
    @JvmStatic
    private val mRetrofitBuilder = Retrofit.Builder()

    private fun setupRetrofit(retrofitBuilder: Retrofit.Builder, client: OkHttpClient, gson: Gson? = null): Retrofit {
        return retrofitBuilder
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(if (gson != null) GsonConverterFactory.create(gson) else GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @JvmStatic
    fun <T>create(service: Class<T>): T {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val mHttpClient = mHttpClientBuilder.addInterceptor { chain ->
            val req = chain.request()
            chain.proceed(
                req.newBuilder()
                    .url(
                        req.url()
                            .newBuilder()
                            .addQueryParameter("api_key", THE_MOVIE_DB_API_KEY)
                            .build()
                    ).build()
            )
        }
            .addInterceptor(loggingInterceptor)
            .build()
        return setupRetrofit(mRetrofitBuilder, mHttpClient)
            .create(service)
    }
}
