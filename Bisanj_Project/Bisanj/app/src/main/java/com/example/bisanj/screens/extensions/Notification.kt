package com.example.bisanj.screens.extensions

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.bisanj.R

const val CHANNEL_ID = "channel id"
const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(context: Context) {
    val builder = NotificationCompat.Builder(
        context,
        CHANNEL_ID
    )

    builder
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("Bisanj")
        .setContentText("Don't forget to test your self with our application!")

    notify(NOTIFICATION_ID, builder.build())
}