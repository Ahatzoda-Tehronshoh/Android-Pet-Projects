package com.example.somoni.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.somoni.MainActivity
import com.example.somoni.R
import com.example.somoni.setting.SharedPref
import com.example.somoni.onboarding.OnBoarding
import kotlinx.coroutines.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val context = this
        var intent = Intent(context, MainActivity::class.java)

        if (getSharedPreferences(
                SharedPref.NAME_SHAREDPREFERENCE,
                MODE_PRIVATE
            ).contains(SharedPref.MODE_KEY_SHAREDPREFERENCE)
        ) {
            SharedPref.sharedPreferences = getSharedPreferences(SharedPref.NAME_SHAREDPREFERENCE, MODE_PRIVATE)
            val sharedPref = SharedPref.sharedPreferences!!
            SharedPref.currency =
                sharedPref.getInt(SharedPref.CURRENCY_KEY_SHAREDPREFERENCE, SharedPref.currency)
            SharedPref.mode =
                sharedPref.getBoolean(SharedPref.MODE_KEY_SHAREDPREFERENCE, SharedPref.mode)
            SharedPref.summa = sharedPref.getInt(
                SharedPref.SUMMA_KEY_SHAREDPREFERENCE,
                SharedPref.summa
            )
            SharedPref.theme = sharedPref.getBoolean(
                SharedPref.Theme_KEY_SHAREDPREFERENCE,
                SharedPref.theme
            )
            SharedPref.notifications = sharedPref.getBoolean(
                SharedPref.NOTIFICATION_KEY_SHAREDPREFERENCE,
                SharedPref.notifications
            )
            SharedPref.language = sharedPref.getString(
                SharedPref.LANGUAGE_KEY_SHAREDPREFERENCE,
                SharedPref.language
            )!!
        } else {
            SharedPref.sharedPreferences =
                getSharedPreferences(SharedPref.NAME_SHAREDPREFERENCE, MODE_PRIVATE)
            SharedPref.summa = 1
            SharedPref.language = "ru"
            SharedPref.theme = false
            SharedPref.notifications = false
            intent = Intent(context, OnBoarding::class.java)
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(3000)
            withContext(Dispatchers.Main) {
                startActivity(intent)
                finish()
            }
        }

        if(SharedPref.theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

}