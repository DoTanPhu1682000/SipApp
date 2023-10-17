package com.dotanphu.sipapp.data.event

import java.io.Serializable

class AlertEvent(var title: String, var content: String) : Serializable {
    companion object {
        fun create(title: String, content: String): AlertEvent {
            return AlertEvent(title, content)
        }
    }
}