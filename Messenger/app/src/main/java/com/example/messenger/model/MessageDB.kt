package com.example.messenger.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "message"/*, foreignKeys = [ForeignKey(
        entity = Contact::class,
        parentColumns = ["phone_number"],
        childColumns = ["from_user"],
        onDelete = CASCADE
    ), ForeignKey(
        entity = Contact::class,
        parentColumns = ["phone_number"],
        childColumns = ["to_user"],
        onDelete = CASCADE
    )]*/
)
data class MessageDB(
    @ColumnInfo(name = "from_user")
    val fromUser: String = "",
    @ColumnInfo(name = "to_user")
    val toUser: String = "",
    val text: String = "",
    @PrimaryKey
    @ColumnInfo(name = "date")
    val date: Long = 0L
)