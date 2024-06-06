package com.example.messenger.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
data class Contact(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "phone_number")
    @PrimaryKey
    var phoneNumber: String,
    @ColumnInfo(name = "image_url")
    var icon: String? = null
)