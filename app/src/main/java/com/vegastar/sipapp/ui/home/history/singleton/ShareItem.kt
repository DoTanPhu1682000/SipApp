package com.vegastar.sipapp.ui.home.history.singleton

import org.linphone.core.CallLog

class ShareItem {
    companion object {
        private var mInstance: ShareItem? = null
        var callLogs: ArrayList<CallLog>? = null

        @Synchronized
        fun getInstance(): ShareItem? {
            if (mInstance == null) mInstance = ShareItem()
            return mInstance
        }
    }
}