package com.example.messenger.repository

import com.example.messenger.MessengerLogger
import com.example.messenger.data.remote.NetworkResult
import com.example.messenger.model.Contact
import com.example.messenger.model.Message
import com.example.messenger.model.MessageDB
import com.example.messenger.model.User
import com.example.messenger.util.currentUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RemoteRepository(val databaseRepository: DatabaseRepository) {
    private val firebaseDbRef = FirebaseDatabase.getInstance().reference

    suspend fun addUserIfNotExist(networkRequestStatus: (NetworkResult<Boolean>) -> (Unit)) {
        firebaseDbRef.child("user")
            .child(currentUser.phoneNumber)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (!snapshot.exists()) {
                            firebaseDbRef.child("user").child(currentUser.phoneNumber)
                                .setValue(User(currentUser.name))
                        }

                        databaseRepository.insertContacts(listOf(currentUser))

                        networkRequestStatus(NetworkResult.Success(true))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    networkRequestStatus(
                        NetworkResult.Error(
                            error.message,
                            false
                        )
                    )
                }

            })
    }

    suspend fun getListOfChats(networkRequestStatus: (NetworkResult<Boolean>) -> (Unit)) {
        firebaseDbRef.child("chats")
            .child(currentUser.phoneNumber)
            .child("user_chat")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val listOfContacts = mutableListOf<Contact>()
                        for (postSnapshot in snapshot.children)
                            listOfContacts.add(
                                Contact(
                                    phoneNumber = postSnapshot.key ?: "",
                                    name = ((postSnapshot.value as HashMap<String, String>)["name"]
                                        ?: "")
                                )
                            )

                        listOfContacts.filter {
                            it.phoneNumber != "" && it.name != ""
                        }

                        databaseRepository.apply {
                            clearListOfContacts()
                            insertContacts(listOfContacts)
                        }
                        networkRequestStatus(NetworkResult.Success(true))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    networkRequestStatus(NetworkResult.Error(error.message))
                    MessengerLogger.logError(error.message)
                }
            })
    }

    suspend fun getMessagesOfChats(
        contactId: String,
        networkRequestStatus: (NetworkResult<Boolean>) -> (Unit)
    ) {
        firebaseDbRef.child("chats")
            .child(currentUser.phoneNumber)
            .child("chat")
            .child(contactId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val listOfMessages = mutableListOf<MessageDB>()

                        for (postSnapshot in snapshot.children)
                            postSnapshot.getValue<Message>(Message::class.java)?.let {
                                listOfMessages.add(
                                    MessageDB(
                                        fromUser = if (it.sent) currentUser.phoneNumber else contactId,
                                        toUser = if (!it.sent) currentUser.phoneNumber else contactId,
                                        text = it.text,
                                        date = it.date
                                    )
                                )
                            }

                        // Transaction - first clear, then insert new list of messages
                        databaseRepository
                            .updateMessagesOfChat(
                                currentUser.phoneNumber,
                                contactId,
                                listOfMessages
                            )

                        networkRequestStatus(NetworkResult.Success(true))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    networkRequestStatus(NetworkResult.Error(error.message))
                    MessengerLogger.logError(error.message)
                }
            })
    }

}