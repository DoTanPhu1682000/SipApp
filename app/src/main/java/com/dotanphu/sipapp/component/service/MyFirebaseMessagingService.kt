package com.dotanphu.sipapp.component.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.ui.call.IncomingCallActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var dataManager: DataManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("MyFirebaseMessagingService", "onNewToken")

        if (!TextUtils.isEmpty(token)) {
            dataManager.mPreferenceHelper.fcmToken = token
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        LogUtil.wtf("onMessageReceived")

        var payload: Map<String, String>? = null
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            payload = remoteMessage.data
            LogUtil.`object`(payload)

            payLoadNotification(payload)
        }

        remoteMessage.notification?.body?.let { messageBody ->
            Log.e("MyFirebaseMessagingService", "Message Notification Body: $messageBody")
            sendNotification(messageBody)
        }
    }

    private fun payLoadNotification(payload: Map<String, String>) {
        startPrepareCallService()
    }

    private fun startPrepareCallService() {
        if (dataManager.mPreferenceHelper.isLogin) {
            PrepareCallService.start(applicationContext)
        }
    }

    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, IncomingCallActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_question_brown)
            .setContentTitle("FCM Message")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}