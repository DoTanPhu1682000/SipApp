package com.dotanphu.sipapp.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.component.receiver.RejectCallActionReceiver
import com.dotanphu.sipapp.ui.call.IncomingCallActivity
import com.dotanphu.sipapp.utils.constant.KeyConstant.KEY_ACTION
import com.dotanphu.sipapp.utils.constant.KeyConstant.KEY_CALL_REJECT
import com.dotanphu.sipapp.utils.constant.KeyConstant.KEY_OBJECT
import com.dotanphu.sipapp.utils.constant.KeyConstant.KEY_UUID
import com.utils.LogUtil

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
        val channelId = context.getString(R.string.default_notification_channel_id_3)
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

    fun createChannelReminderNotification(context: Context, notificationManager: NotificationManager): String {
        //Tạo kênh thông báo
        val channelId = context.getString(R.string.reminder_notification_channel_id)
        val channelName = context.getString(R.string.reminder_notification_channel_name)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.setSound(SOUND_ALARM, AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            )
            notificationChannel.enableLights(true)
            notificationChannel.vibrationPattern = VIBRATION_ALARM
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

    /*----------------------------------[CREATE NOTIFICATION]-------------------------------------*/
//    fun createReminderNotification(context: Context, title: String?, content: String?) {
//        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager ?: return
//
//        //Tạo kênh thông báo và return channelId;
//        val channelId = createChannelReminderNotification(context, notificationManager)
//        val requestID = Helper.randInt(1000, 50000)
//
//        //Click thông báo
//        val intent: Intent = ReminderActivity.newIntent(context, true)
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//        val pendingIntent = PendingIntent.getActivity(context, requestID, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
//            .setSmallIcon(R.mipmap.logo_notification)
//            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher))
//            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
//            .setContentTitle(title)
//            .setContentText(content) //.setContentIntent(pendingIntent)
//            //.setFullScreenIntent(pendingIntent, true)
//            .setSound(SOUND_ALARM)
//            .setVibrate(VIBRATION_ALARM)
//            .setCategory(NotificationCompat.CATEGORY_REMINDER)
//            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//        //Android 5 không hỗ trợ click setFullScreenIntent
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) builder.setFullScreenIntent(pendingIntent, true) else builder.setContentIntent(pendingIntent)
//        notificationManager.notify(requestID, builder.build())
//    }

//    fun createFirebaseNotification(context: Context, title: String?, content: String?, data: Map<String?, String?>?) {
//        LogUtil.wtf("createFirebaseNotification: %s - %s", title, content)
//        val action = if (data != null && data.containsKey(KEY_ACTION)) data[KEY_ACTION] else null
//        val `object` = if (data != null && data.containsKey(KEY_OBJECT)) data[KEY_OBJECT] else null
//        val uuid = if (data != null && data.containsKey(KEY_UUID)) data[KEY_UUID] else null
//        val notifyId = Helper.randInt(1000, 50000)
//
//        //Click thông báo
//        val intent: Intent = MainActivity.newIntentNotification(context, action, `object`, uuid)
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//        val pendingIntent = PendingIntent.getActivity(context, notifyId, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//        createNotification(context, title, content, notifyId, pendingIntent)
//    }

    fun createNotification(context: Context, title: String?, content: String?, notifyId: Int, pendingIntent: PendingIntent?) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager ?: return

        //Tạo kênh thông báo và return channelId;
        val channelId = createChannelTypeNotification(context, notificationManager)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_asterisk_red_bold)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_asterisk_red_bold))
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setContentIntent(pendingIntent)
            .setContentTitle(Helper.getTextNotNull(title!!))
            .setContentText(Helper.getTextNotNull(content!!).trim()
            ) //.setFullScreenIntent(pendingIntent, true)
            //.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
            .setCategory(NotificationCompat.CATEGORY_PROMO)
            .setSound(SOUND_NOTIFICATION)
            .setVibrate(VIBRATION_NOTIFICATION)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notificationManager.notify(notifyId, builder.build())
    }

    /*----------------------------------[CALL]----------------------------------------------------*/
    fun createIncomingCallNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager ?: return

        //Tạo kênh thông báo và return channelId;
        val channelId = createChannelIncomingCallNotification(context, notificationManager)

        //Click thông báo
        val intent: Intent = IncomingCallActivity.newIntent(context)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context, Helper.randInt(1000, 50000), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val acceptIntent: Intent = IncomingCallActivity.newIntent(context, true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val acceptPendingIntent = PendingIntent.getActivity(context, Helper.randInt(1000, 50000), acceptIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val rejectIntent = Intent(context, RejectCallActionReceiver::class.java)
        rejectIntent.action = KEY_CALL_REJECT
        val rejectPendingIntent = PendingIntent.getBroadcast(context, Helper.randInt(1000, 50000), rejectIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary)) //.setCustomContentView(notificationView)
            //.setContentIntent(pendingIntent)
            //.setFullScreenIntent(pendingIntent, true)
            .addAction(0, context.getString(R.string.accept), acceptPendingIntent)
            .addAction(0, context.getString(R.string.reject), rejectPendingIntent)
            //.setContentTitle(alias)
            .setContentText(context.getString(R.string.text_incoming_video_call))
            .setOngoing(true)
            .setTimeoutAfter((5 * 60 * 1000).toLong()) //timeout 10 min
            .setSound(SOUND_RINGTONE)
            .setVibrate(VIBRATION_RINGTONE)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        //Android 5 không hỗ trợ click setFullScreenIntent
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) builder.setFullScreenIntent(pendingIntent, true) else builder.setContentIntent(pendingIntent)
        notificationManager.notify(NOTIFY_ID_INCOMING_CALL, builder.build())
    }

//    fun createMissedCallNotification(context: Context, calledFullName: String?) {
//        val title = calledFullName ?: context.getString(R.string.app_name)
//        val content = context.getString(R.string.missed_call)
//        val notifyId = Helper.randInt(1000, 50000)
//
//        //Click thông báo
//        val intent: Intent = MainActivity.newIntent(context)
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//        val pendingIntent = PendingIntent.getActivity(context, notifyId, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//        createNotification(context, title, content, notifyId, pendingIntent)
//    }

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