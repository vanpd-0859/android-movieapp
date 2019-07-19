package com.sun.movieapp.network

import com.sun.movieapp.utils.BASE_URL
import com.sun.movieapp.utils.THE_MOVIE_DB_API_KEY
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Network {
    @JvmStatic
    private val mHttpClientBuilder = OkHttpClient.Builder();
    @JvmStatic
    private val mRetrofitBuilder = Retrofit.Builder()

    private fun setupRetrofit(retrofitBuilder: Retrofit.Builder, client: OkHttpClient): Retrofit {
        return retrofitBuilder
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @JvmStatic
    fun <T>create(service: Class<T>): T {
        val mHttpClient = mHttpClientBuilder.addInterceptor(object: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val mOriginalRequest = chain.request()
                val mOriginalHttpUrl = mOriginalRequest.url()
                val mUrl = mOriginalHttpUrl.newBuilder()
                    .addQueryParameter("api_key", THE_MOVIE_DB_API_KEY)
                    .build()
                val mRequest = mOriginalRequest.newBuilder()
                    .url(mUrl).build()
                return chain.proceed(mRequest)
            }
        }).build()
        return setupRetrofit(mRetrofitBuilder, mHttpClient)
            .create(service)
    }
}
