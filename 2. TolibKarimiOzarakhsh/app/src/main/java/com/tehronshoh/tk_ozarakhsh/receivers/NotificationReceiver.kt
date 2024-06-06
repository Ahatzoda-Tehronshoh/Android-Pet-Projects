package com.tehronshoh.tk_ozarakhsh.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.getSystemService
import com.tehronshoh.tk_ozarakhsh.repository.SharedPreferencesRepository
import com.tehronshoh.tk_ozarakhsh.utils.NOTIFICATION_REMINDER_CHANNEL_ID
import com.tehronshoh.tk_ozarakhsh.utils.SHER_NOTIFICATION_ID
import com.tehronshoh.tk_ozarakhsh.utils.sendNotification

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent) {
        if (SharedPreferencesRepository.getInstance().allowNotification) {
            val manager = getSystemService(
                p0,
                NotificationManager::class.java
            ) as NotificationManager

            manager.cancelAll()
            manager.sendNotification(
                "Вақти  хондан!",
                "Test Notification!",
                NOTIFICATION_REMINDER_CHANNEL_ID,
                SHER_NOTIFICATION_ID,
                applicationContext = p0
            )
        }
    }
}