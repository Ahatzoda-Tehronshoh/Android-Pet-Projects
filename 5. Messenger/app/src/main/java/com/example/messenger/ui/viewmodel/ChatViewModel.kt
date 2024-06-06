package com.example.messenger.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.messenger.data.remote.NetworkResult
import com.example.messenger.model.Contact
import com.example.messenger.model.Message
import com.example.messenger.model.MessageDB
import com.example.messenger.model.User
import com.example.messenger.repository.DatabaseRepository
import com.example.messenger.repository.RemoteRepository
import com.example.messenger.util.FcmFirebaseSender
import com.example.messenger.util.NetworkMonitor
import com.example.messenger.util.currentUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val dbReference = FirebaseDatabase.getInstance().reference

    private val networkMonitor = NetworkMonitor.get()
    val ifNetworkStatusChanged
        get() = networkMonitor.isConnected.asLiveData()
    private val isNetworkConnectedLastCheck
        get() = networkMonitor.lastCheck

    private val databaseRepository = DatabaseRepository(application)
    private val remoteRepository = RemoteRepository(databaseRepository)

    private var _sendMessage = MutableLiveData<NetworkResult<Boolean>>()
    val sendMessage: LiveData<NetworkResult<Boolean>>
        get() = _sendMessage

    private var _messageListRequestStatus = MutableLiveData<NetworkResult<Boolean>>()
    val messageListRequestStatus: LiveData<NetworkResult<Boolean>>
        get() = _messageListRequestStatus

    var editText: String = ""

    fun getMessageList(contactId: String): LiveData<List<MessageDB>> {
        if (isNetworkConnectedLastCheck)
            _messageListRequestStatus.postValue(NetworkResult.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            remoteRepository.getMessagesOfChats(contactId) {
                _messageListRequestStatus.postValue(it)
            }
        }

        return databaseRepository.getMessagesOfChat(currentUser.phoneNumber, contactId)
    }

    fun sendMessage(message: Message, toUser: Contact) {
        if (isNetworkConnectedLastCheck)
            _sendMessage.postValue(NetworkResult.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            // if chat with this user does not exist, add!
            checkForHavingChat(toUser, currentUser)
            checkForHavingChat(currentUser, toUser)

            sendMessage(currentUser, toUser, message)
            message.sent = false
            sendMessage(toUser, currentUser, message)
        }
    }

    private fun sendMessage(fromUser: Contact, toUser: Contact, message: Message) {
        dbReference.child("chats")
            .child(fromUser.phoneNumber)
            .child("chat")
            .child(toUser.phoneNumber)
            .child(message.date.toString())
            .setValue(message)
            .addOnSuccessListener {
                _sendMessage.postValue(NetworkResult.Success(true))
            }
            .addOnFailureListener {
                _sendMessage.postValue(NetworkResult.Error(it.message.toString()))
            }
    }

    private fun checkForHavingChat(contact: Contact, withContact: Contact) {
        dbReference
            .child("chats")
            .child(contact.phoneNumber)
            .child("user_chat")
            .child(withContact.phoneNumber)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        dbReference
                            .child("chats")
                            .child(contact.phoneNumber)
                            .child("user_chat")
                            .child(withContact.phoneNumber)
                            .setValue(User(withContact.name))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _sendMessage.postValue(NetworkResult.Error(error.message))
                }

            })
    }
}