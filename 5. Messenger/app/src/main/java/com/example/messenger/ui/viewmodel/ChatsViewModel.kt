package com.example.messenger.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.messenger.data.remote.NetworkResult
import com.example.messenger.model.Contact
import com.example.messenger.repository.DatabaseRepository
import com.example.messenger.repository.RemoteRepository
import com.example.messenger.util.NetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatsViewModel(application: Application) : AndroidViewModel(application) {
    private val networkMonitor = NetworkMonitor.get()
    val ifNetworkStatusChanged
        get() = networkMonitor.isConnected.asLiveData()

    private val isNetworkConnectedLastCheck
        get() = networkMonitor.lastCheck

    private val databaseRepository = DatabaseRepository(application)
    private val remoteRepository = RemoteRepository(databaseRepository)

    private var _chatsListRequestStatus = MutableLiveData<NetworkResult<Boolean>>()
    val chatsListRequestStatus: LiveData<NetworkResult<Boolean>>
        get() = _chatsListRequestStatus

    var searchingContact = ""
    var listOfContacts: List<Contact> = listOf()

    private var _addUserIfNotExistResponse = MutableLiveData<NetworkResult<Boolean>>()
    val addUserIfNotExistResponse: LiveData<NetworkResult<Boolean>>
        get() = _addUserIfNotExistResponse

    fun addUserIfNotExist() {
        if (isNetworkConnectedLastCheck)
            _addUserIfNotExistResponse.postValue(NetworkResult.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            remoteRepository.addUserIfNotExist {
                _addUserIfNotExistResponse.postValue(it)
            }
        }
    }

    fun search(text: String): List<Contact> =
        listOfContacts.filter {
            (it.name.lowercase().contains(text.lowercase()) || it.phoneNumber.split(" ")
                .joinToString().contains(text))
        }

    fun getListOfChats(): LiveData<List<Contact>> {
        if (isNetworkConnectedLastCheck)
            _chatsListRequestStatus.postValue(NetworkResult.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            remoteRepository.getListOfChats {
                _chatsListRequestStatus.postValue(it)
            }
        }

        return databaseRepository.getListOfContacts()
    }

}