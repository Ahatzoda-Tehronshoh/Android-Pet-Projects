package com.example.bisanj.screens.activity

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.bisanj.R
import com.example.bisanj.SettingFonChangeFragment
import com.example.bisanj.databinding.ActivityMainBinding
import com.example.bisanj.receivers.NotificationAlarmBroadCast
import com.example.bisanj.screens.ResultTestFragment
import com.example.bisanj.screens.extensions.CHANNEL_ID
import com.example.bisanj.screens.extensions.sendNotification
import com.example.bisanj.screens.extensions.utils.SHARED_PREFERENCES_KEY
import com.example.bisanj.screens.extensions.utils.SHARED_PREFERENCES_NOTIFICATION_KEY
import com.example.bisanj.screens.extensions.utils.fon
import com.example.bisanj.screens.extensions.utils.isNotificationSet
import java.text.DateFormat
import java.util.*
import java.util.Calendar.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getSharedPreferencesData()
        setSupportActionBar(binding.toolbar)
        setupNavigation()
        destinationChange()
    }

    private fun setNotificationSettings() {
        val notificationManager =
            (getSystemService(NotificationManager::class.java) as NotificationManager)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Не забудь себя проверить!",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.apply {
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(channel)
        }


        val alarmManager = (getSystemService(ALARM_SERVICE) as AlarmManager)

        val date = Calendar.getInstance()
        if(date.get(HOUR_OF_DAY) > 20) {
            date.set(DAY_OF_MONTH, date.get(DAY_OF_MONTH) + 1)
        }
        date.apply {
            set(HOUR_OF_DAY, 20)
            set(MINUTE, 30)
            set(SECOND, 0)
        }

        Log.d("TAG_TEST", date.time.toString())

        alarmManager.setRepeating(
            RTC_WAKEUP,
            date.timeInMillis,
            3600000L * 24L,
            PendingIntent.getBroadcast(
                this,
                0,
                Intent(this, NotificationAlarmBroadCast::class.java),
                0
            )
        )

        getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE)
            .edit()
            .putBoolean(SHARED_PREFERENCES_NOTIFICATION_KEY, true)
            .commit()
    }

    private fun getSharedPreferencesData() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE)
        if (sharedPreferences.contains(SettingFonChangeFragment.SHARED_PREFERENCES_FON_KEY)) {
            fon = sharedPreferences.getInt(SettingFonChangeFragment.SHARED_PREFERENCES_FON_KEY, fon)
            isNotificationSet =
                sharedPreferences.getBoolean(SHARED_PREFERENCES_NOTIFICATION_KEY, isNotificationSet)
        }
        if (!isNotificationSet) {
            setNotificationSettings()
        }
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.global_nav_graph)
        navController.graph = graph
        NavigationUI.setupWithNavController(findViewById(R.id.toolbar), navController, null)
    }

    //visible arrow back
    private fun destinationChange() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.visibility = when (destination.id) {
                R.id.startFragment -> GONE
                R.id.insertNameFragment -> VISIBLE
                R.id.settingsTestFragment -> GONE
                else -> GONE
            }
        }
    }
}