package com.vegastar.sipapp.utils.core

interface CallStateChangeListener {
    fun onCallStateChanged(isConnected: Boolean)
}