package com.tehronshoh.tk_ozarakhsh.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE

class SharedPreferencesRepository private constructor(context: Context) {
    private val preferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE)

    var fontSize = 18
    //var language = "ru"
    var allowNotification = false
    var themeModeIsNight = false
    var isFirstLaunch = true

    init {
        if (preferences.contains("isFirstLaunch"))
            isFirstLaunch = preferences.getBoolean("isFirstLaunch", false)
        else
            preferences
                .edit()
                .putBoolean("isFirstLaunch", true)
                .apply()

        if (preferences.contains(FONT_SIZE))
            fontSize = preferences.getInt(FONT_SIZE, 18)
        else
            preferences
                .edit()
                .putInt(FONT_SIZE, fontSize)
                .apply()

        /*if (preferences.contains(LANGUAGE))
            language = preferences.getString(LANGUAGE, "ru") ?: "ru"
        else {
            val androidLanguage = Locale.getDefault().language

            language = if (androidLanguage != "ru") "en" else "ru"
            preferences
                .edit()
                .putString(LANGUAGE, language)
                .apply()
        }*/

        if(preferences.contains(NOTIFICATION))
            allowNotification = preferences.getBoolean(NOTIFICATION, false)
        else
            preferences
                .edit()
                .putBoolean(NOTIFICATION, allowNotification)
                .apply()

        if(preferences.contains(THEME_MODE))
            themeModeIsNight = preferences.getBoolean(THEME_MODE, false)
        else
            preferences
                .edit()
                .putBoolean(THEME_MODE, themeModeIsNight)
                .apply()
    }

    fun changeFontSize(newFontSize: Int) {
        fontSize = newFontSize
        preferences
            .edit()
            .putInt(FONT_SIZE, fontSize)
            .apply()
    }

    fun changeFirstLaunch(isFirstLaunch: Boolean) {
        this.isFirstLaunch = isFirstLaunch
        preferences
            .edit()
            .putBoolean("isFirstLaunch", isFirstLaunch)
            .apply()
    }

    fun changeSettingsNotification(allowNotification: Boolean) {
        this.allowNotification = allowNotification
        preferences
            .edit()
            .putBoolean(NOTIFICATION, this.allowNotification)
            .apply()
    }

    fun changeThemeMode(nightMode: Boolean) {
        themeModeIsNight = nightMode
        preferences
            .edit()
            .putBoolean(THEME_MODE, themeModeIsNight)
            .apply()
    }

    companion object {
        private const val SHARED_PREFERENCES_KEY = "shared_preferences_key"

        private const val FONT_SIZE = "font_size"
        //private const val LANGUAGE = "language"
        private const val NOTIFICATION = "notification"
        private const val THEME_MODE = "theme_mode"

        @Volatile
        private lateinit var INSTANCE: SharedPreferencesRepository

        fun init(context: Context) {
            synchronized(this) {
                INSTANCE = SharedPreferencesRepository(context)
            }
        }

        fun getInstance(): SharedPreferencesRepository = INSTANCE
    }
}