package com.vegastar.sipapp.ui.home.history.data

import org.linphone.core.CallLog
import java.io.Serializable

class GroupedCallLogData(callLog: CallLog) : Serializable {
    val callLogs = arrayListOf(callLog)

    var lastCallLog: CallLog = callLog
    var lastCallLogId: String? = callLog.callId
    var lastCallLogStartTimestamp: Long = callLog.startDate

    fun updateLastCallLog(callLog: CallLog) {
        lastCallLog = callLog
        lastCallLogId = callLog.callId
        lastCallLogStartTimestamp = callLog.startDate
    }
}