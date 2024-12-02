package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.data.ApiInterface
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.data.bearerToken
import okhttp3.OkHttpClient


object RetrofitInstance {

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $bearerToken")
                .build()
            chain.proceed(request)
        }
        .build()

    val api: ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl(Util.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}