package com.app.starterkit.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private var retrofit: Retrofit? = null

    val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val httpClient =
        OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(logger)
            .build()

    fun getRetrofitClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create().asLenient())
                .client(httpClient)
                .build()
        }
        return retrofit!!
    }

    val apiService: ApiInterface by lazy {
//   getRetrofitClient("https://ops.360-play.me").create(ApiInterface::class.java)

//   getRetrofitClient("http://122.160.72.57:2121/api/").create(ApiInterface::class.java)//local
//   getRetrofitClient("http://122.160.72.57:7777/api/").create(ApiInterface::class.java)//local shivam

        getRetrofitClient("http://163.123.182.131/api/").create(ApiInterface::class.java)//live
//     getRetrofitClient("http://94.237.61.190:2121/api/").create(ApiInterface::class.java)//local
//   getRetrofitClient("http://122.160.72.57:1111/api/").create(ApiInterface::class.java)//local

    }
}