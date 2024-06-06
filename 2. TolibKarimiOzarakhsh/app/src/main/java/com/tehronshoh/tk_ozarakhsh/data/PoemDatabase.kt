package com.tehronshoh.tk_ozarakhsh.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Poem::class], version = 2, exportSchema = false)
abstract class PoemDatabase : RoomDatabase() {
    abstract fun getPoemDao(): PoemDao

    companion object {
        @Volatile
        private var poemDatabaseInstance: PoemDatabase? = null

        fun getInstance(context: Context): PoemDatabase {
            return synchronized(this) {
                poemDatabaseInstance ?: Room.databaseBuilder(
                    context,
                    PoemDatabase::class.java,
                    "database"
                )
                    .createFromAsset("databases/poem_db.db")
                    .build().also {
                        poemDatabaseInstance = it
                    }
            }
        }
    }
}