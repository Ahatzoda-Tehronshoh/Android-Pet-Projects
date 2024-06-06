package com.tehronshoh.tk_ozarakhsh.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Poem(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val title: String? = null,
    val text: String,
    @ColumnInfo(defaultValue = "gazal")
    val type: String = "gazal",
    // isFavorite is Int, because SQL Lite db has not got Boolean type(assets -> poem_db.db)
    @ColumnInfo(defaultValue = 0.toString())
    var isFavorite: Int = 0
): Parcelable