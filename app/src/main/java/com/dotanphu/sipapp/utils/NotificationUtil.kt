package com.dotanphu.sipapp.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dotanphu.sipapp.R

object NotificationUtil {
    const val NOTIFY_ID_PREPARE_CALL = 50500
    private const val NOTIFY_ID_INCOMING_CALL = 50501
    private const val TIME_VIBRATE = 700

    //    private static final long[] VIBRATION_NOTIFICATION = new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400}; //1 lần
    private val VIBRATION_NOTIFICATION = longArrayOf(TIME_VIBRATE.toLong(), TIME_VIBRATE.toLong(), TIME_VIBRATE.toLong()) //1 lần
    private val VIBRATION_ALARM = longArrayOf(TIME_VIBRATE.toLong(), TIME_VIBRATE.toLong(), TIME_VIBRATE.toLong(), TIME_VIBRATE.toLong(), TIME_VIBRATE.toLong()) //2 lần
    private val VIBRATION_RINGTONE = longArrayOf(TIME_VIBRATE.toLong(), TIME_VIBRATE.toLong(), TIME_VIBRATE.toLong(), TIME_VIBRATE.toLong(), TIME_VIBRATE.toLong()) //2 lần
    private val SOUND_NOTIFICATION = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    private val SOUND_ALARM = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    private val SOUND_RINGTONE = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL)

    //    private static Vibrator mVibrator;
    //    private static Ringtone mRingtone;
    /*----------------------------------[CREATE CHANNEL]------------------------------------------*/
    fun createChannelTypeNotification(context: Context, notificationManager: NotificationManager): String {
        //Tạo kênh thông báo
        val channelId = context.getString(R.string.default_notification_channel_id)
        val channelName = context.getString(R.string.default_notification_channel_name)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.setSound(SOUND_NOTIFICATION, AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            )
            notificationChannel.enableLights(true)
            notificationChannel.vibrationPattern = VIBRATION_NOTIFICATION
            notificationChannel.enableVibration(true)
            //notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }
        return channelId
    }

    fun createChannelPrepareIncomingCallNotification(context: Context, notificationManager: NotificationManager?): String {
        //Tạo kênh thông báo
        val channelId = context.getString(R.string.call_login_notification_channel_id)
        val channelName = context.getString(R.string.call_login_notification_channel_name)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableVibration(true)
            notificationChannel.setSound(null, null)
            notificationChannel.vibrationPattern = longArrayOf(0L)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        return channelId
    }

    fun createChannelIncomingCallNotification(context: Context, notificationManager: NotificationManager): String {
        val channelId = context.getString(R.string.call_notification_channel_id)
        val channelName = context.getString(R.string.call_notification_channel_name)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.setSound(SOUND_RINGTONE, AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build()
            )
            notificationChannel.enableLights(true)
            notificationChannel.vibrationPattern = VIBRATION_RINGTONE
            notificationChannel.enableVibration(true)
            //notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }
        return channelId
    }

    /*----------------------------------[CALL]----------------------------------------------------*/
    fun createPrepareIncomingCallNotification(context: Context): Notification {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Tạo kênh thông báo và return channelId;
        val channelId = createChannelPrepareIncomingCallNotification(context, notificationManager)

        //Click thông báo
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setColor(ContextCompat.getColor(context.applicationContext, R.color.colorPrimary))
            .setContentIntent(null)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.prepare_for_incoming_call))
            .setVibrate(longArrayOf(0L))
            .setOngoing(true)
            .setSound(null)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        return builder.build()
    }

    /**
     * Ẩn thông báo cuộc gọi đến
     */
    fun cancelIncomingNotification(context: Context) {
//        if (mRingtone != null) {
//            mRingtone.stop();
//            mRingtone = null;
//        }
//        if (mVibrator != null) {
//            mVibrator.cancel();
//            mRingtone = null;
//        }
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancel(NOTIFY_ID_INCOMING_CALL)
    }

    fun cancelPrepareNotification(context: Context) {
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancel(NOTIFY_ID_PREPARE_CALL)
    }
}