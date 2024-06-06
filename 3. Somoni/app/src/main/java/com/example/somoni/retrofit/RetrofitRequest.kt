package com.example.somoni.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitRequest {
    @GET("npcr_bank_rates")
    fun getNormalTranslationList(): Call<List<ResponseObject>>

    @GET("c2c_bank_rates")
    fun getTranslationsFromRussianList(): Call<List<ResponseObject>>

    @GET("nbt_rates")
    fun getNBTList(): Call<List<NBTResponse>>
}

//  https://transfer.humo.tj/currency-app/v1/nbt_rates