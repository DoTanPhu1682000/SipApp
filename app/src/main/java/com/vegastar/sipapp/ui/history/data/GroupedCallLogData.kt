package com.vegastar.sipapp.ui.history.data

import org.linphone.core.CallLog

class GroupedCallLogData(callLog: CallLog) {
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