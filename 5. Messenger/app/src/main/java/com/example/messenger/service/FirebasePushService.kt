package com.example.messenger.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.messenger.R
import com.example.messenger.ui.activity.MainActivity
import com.example.messenger.util.CHANNEL_ID
import com.example.messenger.util.CHANNEL_NAME
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebasePushService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var notificationTitle = ""
        var notificationBody = ""

        // Check if message contains a notification payload
        remoteMessage.notification?.let {
            notificationTitle = it.title.toString()
            notificationBody = it.body.toString()
        }

        // If you want to fire a local notification (that notification on the top of the phone screen)
        // you should fire it from here
        sendLocalNotification(notificationTitle, notificationBody)
    }

    private fun sendLocalNotification(notificationTitle: String?, notificationBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setAutoCancel(true) //Automatically delete the notification
                .setSmallIcon(R.drawable.ic_baseline_message_24) //Notification icon
                .setContentIntent(pendingIntent)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setSound(defaultSoundUri)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(1234, notificationBuilder.build())
    }
}

/*

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                          String  token = Objects.requireNonNull(task.getResult()).getToken();

                        }

                    }
                });

 */