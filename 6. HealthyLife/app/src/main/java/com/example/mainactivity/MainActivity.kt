package com.example.mainactivity

import BaseActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.mainactivity.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_KEY, MODE_PRIVATE)
        val languageFromSharedPreference =
            sharedPreferences.getString(LANGUAGE_KEY, language) ?: "ru"
        if (language != languageFromSharedPreference) {
            language = languageFromSharedPreference
            recreate()
        }
        val themeFromSharedPreferences =
            sharedPreferences.getBoolean(THEME_KEY, isThemeNight)
        if (isThemeNight != themeFromSharedPreferences) {
            isThemeNight = themeFromSharedPreferences
            AppCompatDelegate.setDefaultNightMode(if (isThemeNight) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}