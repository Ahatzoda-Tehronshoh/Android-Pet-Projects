package com.tehronshoh.tk_ozarakhsh.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tehronshoh.tk_ozarakhsh.data.Poem
import com.tehronshoh.tk_ozarakhsh.data.PoemDao
import com.tehronshoh.tk_ozarakhsh.data.PoemDatabase
import com.tehronshoh.tk_ozarakhsh.data.PoemType
import com.tehronshoh.tk_ozarakhsh.utils.getDistinct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val _isLoading = MutableStateFlow(true)
    val isLoading
        get() = _isLoading.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1500)
            _isLoading.value = false
        }
    }

    private val poemDao: PoemDao = PoemDatabase.getInstance(application).getPoemDao()

    private val _onClickLiveData = MutableLiveData<Poem?>()
    val onClickLiveData: LiveData<Poem?>
        get() = _onClickLiveData

    private val _searchGazalLiveData = MutableLiveData<List<Poem>>()
    val searchGazalLiveData: LiveData<List<Poem>>
        get() = _searchGazalLiveData.getDistinct()

    private val _searchSherLiveData = MutableLiveData<List<Poem>>()
    val searchSherLiveData: LiveData<List<Poem>>
        get() = _searchSherLiveData.getDistinct()

    private val _searchRuboiLiveData = MutableLiveData<List<Poem>>()
    val searchRuboiLiveData: LiveData<List<Poem>>
        get() = _searchRuboiLiveData.getDistinct()

    var currentSearchingPoemType = PoemType.GAZAL
    var searchingText = ""

    fun getListOfFavorite(poemType: PoemType): LiveData<List<Poem>> =
        poemDao.getFavoritePoemByType(poemType.toString().lowercase())

    fun getListOfPoemsbyType(poemType: PoemType): LiveData<List<Poem>> =
        poemDao.getPoemsByType(poemType.toString().lowercase())

    fun search(searchingStr: String, defaultPoemType: PoemType = currentSearchingPoemType) {
        searchingText = searchingStr
        poemDao.search(
            defaultPoemType.toString().lowercase(),
            searchingStr
        ).observeForever { poemList ->
            when (defaultPoemType) {
                PoemType.GAZAL -> _searchGazalLiveData.postValue(poemList)
                PoemType.SHER -> _searchSherLiveData.postValue(poemList)
                PoemType.RUBOI -> _searchRuboiLiveData.postValue(poemList)
            }
        }
    }

    fun onPoemClick(poem: Poem?) = _onClickLiveData.postValue(poem)

    fun updatePoem(poem: Poem) {
        viewModelScope.launch(Dispatchers.IO) {
            poemDao.updatePoem(poem)
        }
    }
}