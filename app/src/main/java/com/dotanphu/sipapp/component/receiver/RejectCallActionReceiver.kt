package com.dotanphu.sipapp.component.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dotanphu.sipapp.utils.NotificationUtil
import com.dotanphu.sipapp.utils.constant.KeyConstant.KEY_CALL_ID
import com.dotanphu.sipapp.utils.constant.KeyConstant.KEY_CALL_REJECT
import com.dotanphu.sipapp.utils.core.CoreHelper
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