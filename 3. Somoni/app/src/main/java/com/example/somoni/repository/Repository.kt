package com.example.somoni.repository

import com.example.somoni.retrofit.NBTResponse
import com.example.somoni.retrofit.ResponseObject
import com.example.somoni.retrofit.RetrofitClient
import retrofit2.Call

class Repository {
    private val retrofit = RetrofitClient.getRetrofitRequest()

    fun getNormalTranslationList(): Call<List<ResponseObject>> = retrofit.getNormalTranslationList()

    fun getTranslationsFromRussianList(): Call<List<ResponseObject>> = retrofit.getTranslationsFromRussianList()

    fun getNBTList(): Call<List<NBTResponse>> = retrofit.getNBTList()

}