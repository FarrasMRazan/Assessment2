package com.farrasmuhammadrazan0100.assement2.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ManhwaApiConfig {

    private val okHttpClient: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val shiroService: ManhwaApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://shiro.kuuhaku.space/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ManhwaApiService::class.java)
    }

    val collectionService: ManhwaApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://shiro.kuuhaku.space/") // ← ganti URL server kamu
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ManhwaApiService::class.java)
    }
}