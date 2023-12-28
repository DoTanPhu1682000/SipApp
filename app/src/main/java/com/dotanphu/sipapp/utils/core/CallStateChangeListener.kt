package com.dotanphu.sipapp.utils.core

interface CallStateChangeListener {
    fun onCallStateChanged(isConnected: Boolean)
}