package com.tehronshoh.tk_ozarakhsh.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.tehronshoh.tk_ozarakhsh.R
import com.tehronshoh.tk_ozarakhsh.ui.activities.MainActivity

const val NOTIFICATION_REMINDER_CHANNEL_ID = "com.tehronshoh.ozarakhsh.channel_id"
const val NOTIFICATION_PUSH_CHANNEL_ID = "com.tehronshoh.ozarakhsh.push_channel_id"

const val SHER_NOTIFICATION_ID = 123

fun NotificationManager.sendNotification(
    title: String,
    messageBody: String,
    notificationChannelId: String,
    notificationId: Int = SHER_NOTIFICATION_ID,
    pendingIntent: PendingIntent? = null,
    applicationContext: Context
) {
    var contentPendingIntent = pendingIntent

    if(contentPendingIntent == null) {
        val contentIntent = Intent(applicationContext, MainActivity::class.java)
        contentIntent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            notificationId,
            contentIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                PendingIntent.FLAG_IMMUTABLE
            else
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    val builder =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            NotificationCompat.Builder(applicationContext, notificationChannelId)
        else
            NotificationCompat.Builder(applicationContext)

    builder
        .setSmallIcon(R.drawable.ashor_item_icon)
        .setContentTitle(title)
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    notify(notificationId, builder.build())
}
