package com.example.messenger.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.messenger.model.Contact
import com.example.messenger.model.MessageDB

@Dao
interface DataDAO {
    @Query("SELECT * from contact where phone_number != :currentUser;")
    fun getListOfChats(currentUser: String): LiveData<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(contact: List<Contact>)

    @Query("DELETE FROM contact;")
    fun clearContactList()

    @Transaction
    fun updateMessagesOfChat(firstUserId: String, secondUserId: String, listOfMessages: List<MessageDB>) {
        clearMessagesOfChat(firstUserId, secondUserId)
        insertMessage(listOfMessages)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMessage(message: List<MessageDB>)

    @Query("SELECT * FROM message where (from_user = :firstUserId and to_user = :secondUserId) or (from_user = :secondUserId and to_user = :firstUserId);")
    fun getMessagesOfChat(firstUserId: String, secondUserId: String): LiveData<List<MessageDB>>

    @Query("DELETE FROM MESSAGE WHERE (from_user = :firstUserId and to_user = :secondUserId) or (from_user = :secondUserId and to_user = :firstUserId);")
    fun clearMessagesOfChat(firstUserId: String, secondUserId: String)
}