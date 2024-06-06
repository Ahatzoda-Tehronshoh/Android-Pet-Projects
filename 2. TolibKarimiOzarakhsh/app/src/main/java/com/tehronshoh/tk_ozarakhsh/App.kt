package com.tehronshoh.tk_ozarakhsh

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.tehronshoh.tk_ozarakhsh.repository.SharedPreferencesRepository
import com.tehronshoh.tk_ozarakhsh.utils.NOTIFICATION_PUSH_CHANNEL_ID
import com.tehronshoh.tk_ozarakhsh.utils.NOTIFICATION_REMINDER_CHANNEL_ID

class App : Application() {
    override fun onCreate() {
        SharedPreferencesRepository.init(this)
        createNotificationChannel()
        super.onCreate()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelList = listOf(createReminderChannel(), createPushChannel())
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannels(channelList)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createReminderChannel() =
        NotificationChannel(
            NOTIFICATION_REMINDER_CHANNEL_ID,
            "reminder_every_day",
            NotificationManager.IMPORTANCE_HIGH
        )
            .apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "Reading time!"
            }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createPushChannel() =
        NotificationChannel(
            NOTIFICATION_PUSH_CHANNEL_ID,
            "push_if_added_new_poem",
            NotificationManager.IMPORTANCE_HIGH
        )
            .apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = "Reading time!"
            }
}