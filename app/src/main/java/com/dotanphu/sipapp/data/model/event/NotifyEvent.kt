package com.dotanphu.sipapp.data.model.event

class NotifyEvent(var type: Type) {
    enum class Type {
        DEFAULT, OUTGOING, INCOMING
    }
}