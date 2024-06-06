package com.example.somoni.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.somoni.repository.Repository
import com.example.somoni.retrofit.NBTResponse
import com.example.somoni.setting.SharedPref
import com.example.somoni.retrofit.ResponseObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository()

    private var _normalLiveData: MutableLiveData<List<ResponseObject>> = MutableLiveData()
    val normalLiveData: LiveData<List<ResponseObject>>
        get() = _normalLiveData

    private var _russianLiveData: MutableLiveData<List<ResponseObject>> = MutableLiveData()
    val russianLiveData: LiveData<List<ResponseObject>>
        get() = _russianLiveData

    private var _nbtLiveData: MutableLiveData<List<NBTResponse>> = MutableLiveData()
    val nbtLiveData: LiveData<List<NBTResponse>>
        get() = _nbtLiveData

    fun getTranslationsFromRussianList() {
        viewModelScope.launch {
            repository.getTranslationsFromRussianList()
                .enqueue(object : Callback<List<ResponseObject>> {
                    override fun onResponse(
                        call: Call<List<ResponseObject>>,
                        response: Response<List<ResponseObject>>
                    ) {
                        if (!response.isSuccessful) {
                            Log.d("ERROR", "${response.code()}")
                            _russianLiveData.postValue(listOf())
                        }

                        if (response.body() != null) {
                            _russianLiveData.postValue(
                                sortNormalList(
                                    response.body() as MutableList<ResponseObject>,
                                    false
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<List<ResponseObject>>, t: Throwable) {
                        Log.d("ERROR", "error with retrofit onFailure!!!")
                        _russianLiveData.postValue(listOf())
                    }
                })
        }
    }

    fun getNormalTranslationList() {
        viewModelScope.launch {
            repository.getNormalTranslationList()
                .enqueue(object : Callback<List<ResponseObject>> {
                    override fun onResponse(
                        call: Call<List<ResponseObject>>,
                        response: Response<List<ResponseObject>>
                    ) {
                        if (!response.isSuccessful) {
                            Log.d("TEST_TAG_ERROR", "${response.code()}")
                            _normalLiveData.postValue(listOf())
                        }

                        if (response.body() != null) {
                            _normalLiveData.postValue(
                                sortNormalList(
                                    response.body() as MutableList<ResponseObject>,
                                    true
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<List<ResponseObject>>, t: Throwable) {
                        Log.d("ERROR", "error with retrofit onFailure!!!")
                        _normalLiveData.postValue(listOf())
                    }
                })
        }
    }

    fun getNBTList() {
        viewModelScope.launch {
            repository.getNBTList()
                .enqueue(object : Callback<List<NBTResponse>> {
                    override fun onResponse(
                        call: Call<List<NBTResponse>>,
                        response: Response<List<NBTResponse>>
                    ) {
                        if (!response.isSuccessful)
                            Log.d("ERROR", "${response.code()}")

                        if (response.body() != null) {
                            _nbtLiveData.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<List<NBTResponse>>, t: Throwable) {
                        Log.d("ERROR", "error with retrofit onFailure!!!")
                    }

                })
        }
    }

    private fun sortNormalList(
        list: MutableList<ResponseObject>,
        bool: Boolean
    ): List<ResponseObject> {
        val size = list.size - 1
        val currency = if (bool) SharedPref.currency else 0
        for (i in 0 until size) {
            for (j in (i + 1)..size) {
                if (list[i].currency!![currency].buyValue!!.toFloat() < list[j].currency!![currency].buyValue!!.toFloat()) {
                    val a = list[i]
                    list[i] = list[j]
                    list[j] = a
                }
            }
        }

        return list
    }
}