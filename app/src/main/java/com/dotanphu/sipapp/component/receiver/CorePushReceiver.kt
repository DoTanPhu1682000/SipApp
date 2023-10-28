package com.dotanphu.sipapp.component.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.utils.LogUtil

class CorePushReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        LogUtil.wtf("[Push Notification] Push notification has been received in broadcast receiver")
    }
}