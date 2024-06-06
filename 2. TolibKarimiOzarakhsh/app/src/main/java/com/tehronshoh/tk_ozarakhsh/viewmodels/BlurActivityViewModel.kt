package com.tehronshoh.tk_ozarakhsh.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tehronshoh.tk_ozarakhsh.data.Poem
import com.tehronshoh.tk_ozarakhsh.data.PoemDao
import com.tehronshoh.tk_ozarakhsh.data.PoemDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlurActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val poemDao: PoemDao = PoemDatabase.getInstance(application).getPoemDao()

    fun updatePoem(poem: Poem) {
        viewModelScope.launch(Dispatchers.IO) {
            poemDao.updatePoem(poem)
        }
    }
}
