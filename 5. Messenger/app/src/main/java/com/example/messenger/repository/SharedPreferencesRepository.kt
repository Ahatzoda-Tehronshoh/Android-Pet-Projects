package com.example.messenger.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.messenger.util.currentUser
import org.intellij.lang.annotations.Language

class SharedPreferencesRepository private constructor(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE)

    var language: String = "ru"
    var themeMode: String = "light"

    init {
        // USER_NAME
        if (preferences.contains(USER_NAME))
            currentUser.name = preferences.getString(USER_NAME, null) ?: ""
        else {
            val editor = preferences.edit()
            editor.putString(USER_NAME, "Your Name")
            editor.apply()
        }

        // PHONE_NUMBER
        if (preferences.contains(PHONE_NUMBER))
            currentUser.phoneNumber = preferences.getString(PHONE_NUMBER, null) ?: ""
        else {
            val editor = preferences.edit()
            editor.putString(PHONE_NUMBER, "+992 92 746 1110")
            editor.apply()
        }

        // LANGUAGE
        if (preferences.contains(APP_LANGUAGE))
            language = preferences.getString(APP_LANGUAGE, null) ?: "ru"
        else {
            val editor = preferences.edit()
            editor.putString(APP_LANGUAGE, "ru")
            editor.apply()
        }

        // THEME_MODE
        if (preferences.contains(THEME_MODE))
            themeMode = preferences.getString(THEME_MODE, null) ?: "light"
        else {
            val editor = preferences.edit()
            editor.putString(THEME_MODE, "light")
            editor.apply()
        }

        // IMAGE
        if (preferences.contains(IMAGE))
            currentUser.icon = preferences.getString(IMAGE, null) ?: ""
        else {
            val editor = preferences.edit()
            editor.putString(IMAGE, "")
            editor.apply()
        }
    }

    fun changeUserName(updatedName: String) {
        val editor = preferences.edit()
        currentUser.name = updatedName
        editor.putString(USER_NAME, updatedName)
        editor.apply()
    }

    fun changePhoneNumber(updatedPhoneNumber: String) {
        val editor = preferences.edit()
        currentUser.phoneNumber = updatedPhoneNumber
        editor.putString(PHONE_NUMBER, updatedPhoneNumber)
        editor.apply()
    }

    fun changeLanguage(updatedLanguage: String) {
        val editor = preferences.edit()
        language = updatedLanguage
        editor.putString(APP_LANGUAGE, updatedLanguage)
        editor.apply()
    }

    fun changeThemeMode(updatedMode: String) {
        val editor = preferences.edit()
        themeMode = updatedMode
        editor.putString(THEME_MODE, updatedMode)
        editor.apply()
    }

    fun changeUserImage(updatedImage: String) {
        val editor = preferences.edit()
        currentUser.icon = updatedImage
        editor.putString(IMAGE, updatedImage)
        editor.apply()
    }

    companion object {
        private const val SHARED_PREFERENCES_KEY = "shared_preferences"
        private const val USER_NAME = "user_name"
        private const val PHONE_NUMBER = "phone_number"
        private const val APP_LANGUAGE = "app_language"
        private const val THEME_MODE = "theme_mode"
        private const val IMAGE = "user_image"

        @Volatile
        private lateinit var sharedPreferencesRepository: SharedPreferencesRepository

        fun init(context: Context) {
            synchronized(this) {
                sharedPreferencesRepository = SharedPreferencesRepository(context)
            }
        }

        fun get(): SharedPreferencesRepository {
            return sharedPreferencesRepository
        }

    }
}