package com.tehronshoh.tk_ozarakhsh.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tehronshoh.tk_ozarakhsh.utils.getDistinct

@Dao
abstract class PoemDao {
    @Query("SELECT * FROM poem WHERE type = :type;")
    protected abstract fun getPoemsByTypeDAO(type: String): LiveData<List<Poem>>

    fun getPoemsByType(type: String): LiveData<List<Poem>> =
        getPoemsByTypeDAO(type).getDistinct()

    @Query("SELECT * FROM poem WHERE type = :type AND isFavorite = 1;")
    abstract fun getFavoritePoemByType(type: String): LiveData<List<Poem>>

    @Query("SELECT * FROM poem where type = :type AND LOWER(text) like '%' || LOWER(:searchingStr) ||'%' order by title;")
    protected abstract fun searchDAO(type: String, searchingStr: String): LiveData<List<Poem>>

    fun search(type: String, searchingStr: String): LiveData<List<Poem>> =
        searchDAO(type, searchingStr).getDistinct()

    @Update
    abstract fun updatePoem(poem: Poem)

    @Insert
    abstract fun insertPoem(addingPoem: Poem)

    @Query("SELECT MAX(id) from poem;")
    abstract fun getLastInsertedPoemId(): Long
}