package com.example.messenger.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.messenger.data.local.DataDB
import com.example.messenger.model.Contact
import com.example.messenger.model.MessageDB
import com.example.messenger.util.currentUser

class DatabaseRepository(context: Context) {
    private val dataDAO = DataDB.getInstance(context).getDataDAO()

    fun getListOfContacts(): LiveData<List<Contact>> {
        return dataDAO.getListOfChats(currentUser.phoneNumber)
    }

    suspend fun clearListOfContacts() {
        dataDAO.clearContactList()
    }

    fun insertContacts(listOfContacts: List<Contact>) {
        dataDAO.insertContact(listOfContacts)
    }

    fun getMessagesOfChat(firstUser: String, secondUser: String): LiveData<List<MessageDB>> {
        return dataDAO.getMessagesOfChat(firstUser, secondUser)
    }

    fun updateMessagesOfChat(firstUser: String, secondUser: String, listOfMessages: List<MessageDB>) {
        dataDAO.updateMessagesOfChat(firstUser, secondUser, listOfMessages)
    }
}