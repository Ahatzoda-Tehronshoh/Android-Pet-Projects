package com.tehronshoh.tk_ozarakhsh.receivers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.tehronshoh.tk_ozarakhsh.R
import com.tehronshoh.tk_ozarakhsh.data.Poem
import com.tehronshoh.tk_ozarakhsh.data.PoemDao
import com.tehronshoh.tk_ozarakhsh.data.PoemDatabase
import com.tehronshoh.tk_ozarakhsh.repository.SharedPreferencesRepository
import com.tehronshoh.tk_ozarakhsh.ui.activities.MainActivity
import com.tehronshoh.tk_ozarakhsh.utils.NOTIFICATION_PUSH_CHANNEL_ID
import com.tehronshoh.tk_ozarakhsh.utils.sendNotification

class FirebasePushService : FirebaseMessagingService() {

    private lateinit var databaseDao: PoemDao

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TAG_TEST", "onNewToken: $token")
    }

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
        SharedPreferencesRepository.getInstance().also {
            if(it.isFirstLaunch) {
                it.changeFirstLaunch(false)
                return@handleIntent
            }
        }

        val notificationTitle = intent?.extras?.getString("gcm.notification.title") ?: "Reminder"
        val notificationBody = intent?.extras?.getString("gcm.notification.body") ?: "Вақти хондан!"

        Log.d("TAG_TEST", "handleIntent: Title $notificationTitle")
        Log.d("TAG_TEST", "handleIntent: Body $notificationBody")

        // TITLE
        /** 🎉Ғазали нав **/
        /** 🎉Шери нав **/
        /** 🎉Рубоии нав **/

        var pendingIntent: PendingIntent? = null

        // If title first char equals to '🎉', it means that added new sher!
        if (notificationTitle.substring(0, 2) == "\uD83C\uDF89") {
            databaseDao = PoemDatabase.getInstance(this).getPoemDao()

            var poem: Poem?

            if (notificationTitle.substring(2, 7) == "Ғазал")
                databaseDao.insertPoem(
                    Poem(
                        title = "${notificationBody.split('\n')[0]}...",
                        text = notificationBody,
                        type = "gazal"
                    ).also {
                        poem = it
                    }
                )
            else if (notificationTitle.substring(2, 5) == "Шер")
                databaseDao.insertPoem(
                    Poem(
                        title = notificationBody.split('\n')[0],
                        text = notificationBody,
                        type = "sher"
                    ).also {
                        poem = it
                    }
                )
            else if (notificationTitle.substring(2, 7) == "Рубои")
                databaseDao.insertPoem(
                    Poem(
                        title = notificationBody,
                        text = notificationBody,
                        type = "ruboi"
                    ).also {
                        poem = it
                    }
                )
            else
                poem = null

            if (poem != null) {
                poem!!.id = databaseDao.getLastInsertedPoemId()
                Log.d("TAG_TEST", "handleIntent: added poem = $poem")

                pendingIntent = NavDeepLinkBuilder(this@FirebasePushService)
                    .setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.gazalSherItemFragment)
                    .setArguments(Bundle().also {
                        it.putParcelable("poem", poem!!)
                    })
                    .createPendingIntent()
            }
        }

        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
            cancelAll()
            sendNotification(
                title = notificationTitle,
                messageBody = notificationBody,
                notificationChannelId = NOTIFICATION_PUSH_CHANNEL_ID,
                pendingIntent = pendingIntent,
                applicationContext = this@FirebasePushService
            )
        }
    }
}