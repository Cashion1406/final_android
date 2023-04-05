package com.example.finalandroid.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl("https://backend-2tza.onrender.com")
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    val api: TestAPI by lazy {
        retrofit.create(TestAPI::class.java)
    }
}