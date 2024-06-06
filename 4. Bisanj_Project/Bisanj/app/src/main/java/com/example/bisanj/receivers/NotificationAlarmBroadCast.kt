package com.example.bisanj.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.bisanj.screens.extensions.sendNotification

class NotificationAlarmBroadCast : BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent) {
        val notificationManager = (ContextCompat.getSystemService(
            p0,
            NotificationManager::class.java
        ) as NotificationManager)

        notificationManager.apply {
            cancelAll()
            sendNotification(p0)
        }
    }
}