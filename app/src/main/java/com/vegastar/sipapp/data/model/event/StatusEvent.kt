package com.vegastar.sipapp.data.model.event

import androidx.annotation.StringRes
import java.io.Serializable

class StatusEvent(var type: Int, var resId: Int, var content: String?) : Serializable {
    companion object {
        const val TYPE_SUCCESS = 1
        const val TYPE_ERROR = 0
        fun create(type: Int, content: String?): StatusEvent {
            return StatusEvent(type, 0, content)
        }

        fun create(type: Int, @StringRes resId: Int): StatusEvent {
            return StatusEvent(type, resId, null)
        }
    }
}