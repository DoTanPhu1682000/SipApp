package com.vegastar.sipapp.data.model.event

import java.io.Serializable

class ProgressEvent(var isShow: Boolean, var content: String?) : Serializable {
    companion object {
        fun create(isShow: Boolean, content: String?): ProgressEvent {
            return ProgressEvent(isShow, content)
        }

        fun create(isShow: Boolean): ProgressEvent {
            return ProgressEvent(isShow, null)
        }

        fun create(content: String?): ProgressEvent {
            return ProgressEvent(false, content)
        }
    }
}