package com.example.somoni.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofitClient: Retrofit = Retrofit.Builder()
        .baseUrl("https://transfer.humo.tj/currency-app/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getRetrofitRequest(): RetrofitRequest = retrofitClient.create(RetrofitRequest::class.java)
}