package com.example.mainactivity

import android.content.SharedPreferences
import android.icu.util.Calendar
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import java.util.*
import java.util.Calendar.*

const val SHARED_PREFERENCE_KEY = "SHARED_PREFERENCE"
const val LANGUAGE_KEY = "LANGUAGE_KEY"
const val THEME_KEY = "THEME_KEY"

var language = "ru"
var isThemeNight = false

fun getNavController(supportFragmentManager: FragmentManager) =
    (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

fun saveLanguage(sharedPreference: SharedPreferences, languageName: String) {
    language = languageName
    sharedPreference
        .edit()
        .putString(LANGUAGE_KEY, languageName)
        .apply()
}

fun saveThemeMode(sharedPreference: SharedPreferences, isNightMode: Boolean) {
    isThemeNight = isNightMode
    sharedPreference
        .edit()
        .putBoolean(THEME_KEY, isNightMode)
        .apply()
}

fun getCurrentDate(
    year: Int = Calendar.getInstance().get(YEAR),
    month: Int = Calendar.getInstance().get(MONTH)+1,
    day: Int = Calendar.getInstance().get(DAY_OF_MONTH)
): String {
    return (if (day < 10) "0$day" else "$day") + ":" + (if (month < 10) "0$month" else "$month") + ":" + year
}
