package com.example.somoni.setting

import android.content.SharedPreferences

object SharedPref {
    var sharedPreferences: SharedPreferences? = null
    const val NAME_SHAREDPREFERENCE = "OnBoardingSharedPreference"
    const val CURRENCY_KEY_SHAREDPREFERENCE = "It's not first time currency!"
    const val MODE_KEY_SHAREDPREFERENCE = "It's not first time mode!"
    const val SUMMA_KEY_SHAREDPREFERENCE = "It's not summa count!"
    const val LANGUAGE_KEY_SHAREDPREFERENCE = "The language variable!"
    const val Theme_KEY_SHAREDPREFERENCE = "The theme is nighMode!"
    const val NOTIFICATION_KEY_SHAREDPREFERENCE = "The notification is turn on!"

    var currency: Int = 1 // 1 -> RUB        0 -> USD            2 -> EUR
    set(value) {
        field = value
        if(sharedPreferences != null) {
            sharedPreferences!!
                .edit()
                .putInt(CURRENCY_KEY_SHAREDPREFERENCE, value)
                .apply()
        }
    }

    var mode = true // true -> simpleMode         false -> longMode
    set(value) {
        field = value
        if(sharedPreferences != null) {
            sharedPreferences!!
                .edit()
                .putBoolean(MODE_KEY_SHAREDPREFERENCE, value)
                .apply()
        }
    }

    var summa = 1
        set(value) {
            field = value
            if(sharedPreferences != null) {
                sharedPreferences!!
                    .edit()
                    .putInt(SUMMA_KEY_SHAREDPREFERENCE, value)
                    .apply()
            }
        }

    var language = "ru"
        set(value) {
            field = value
            if(sharedPreferences != null) {
                sharedPreferences!!
                    .edit()
                    .putString(LANGUAGE_KEY_SHAREDPREFERENCE, value)
                    .apply()
            }
        }

    var theme = false
        set(value) {
            field = value
            if(sharedPreferences != null) {
                sharedPreferences!!
                    .edit()
                    .putBoolean(Theme_KEY_SHAREDPREFERENCE, value)
                    .apply()
            }
        }

    var notifications = false
        set(value) {
            field = value
            if(sharedPreferences != null) {
                sharedPreferences!!
                    .edit()
                    .putBoolean(NOTIFICATION_KEY_SHAREDPREFERENCE, value)
                    .apply()
            }
        }
}