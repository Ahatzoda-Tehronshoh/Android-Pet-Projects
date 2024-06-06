package com.example.messenger.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.messenger.model.Contact
import com.example.messenger.model.MessageDB

@Database(
    entities = [Contact::class, MessageDB::class],
    version = 1,
    exportSchema = false
)
abstract class DataDB : RoomDatabase() {
    abstract fun getDataDAO(): DataDAO

    companion object {
        @Volatile
        private var INSTANCE: DataDB? = null

        fun getInstance(context: Context): DataDB {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context,
                    DataDB::class.java,
                    "data_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }

    }
}