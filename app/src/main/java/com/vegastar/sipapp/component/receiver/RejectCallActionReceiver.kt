package com.vegastar.sipapp.component.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.vegastar.sipapp.utils.NotificationUtil
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_CALL_ID
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_CALL_REJECT
import com.vegastar.sipapp.utils.core.CoreHelper
import com.utils.LogUtil

class RejectCallActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null && intent.action == KEY_CALL_REJECT) {
            LogUtil.wtf("ACTION_CALL_REJECT")

            CoreHelper.getInstance(context)?.hangUpIncomingCall()

            //Dừng dịch vụ và xóa thông báo
            //PrepareCallService.stop(context)
            NotificationUtil.cancelPrepareNotification(context)
            NotificationUtil.cancelIncomingNotification(context)
        }
    }
}