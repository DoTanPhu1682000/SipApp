package com.vegastar.sipapp.data.model.event

import java.io.Serializable

class LoadingEvent(var request: Int, var isShow: Boolean) : Serializable {
    companion object {
        fun create(request: Int, isShow: Boolean): LoadingEvent {
            return LoadingEvent(request, isShow)
        }
    }
}