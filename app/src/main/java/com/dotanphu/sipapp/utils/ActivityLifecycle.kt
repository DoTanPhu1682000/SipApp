package com.dotanphu.sipapp.utils

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

class ActivityLifecycle private constructor() : ActivityLifecycleCallbacks {
    private var isForeground = false

    val isBackground: Boolean
        get() = !isForeground

    companion object {
        @get:Synchronized
        var instance: ActivityLifecycle? = null
            private set

        fun init(app: Application) {
            if (instance == null) {
                instance = ActivityLifecycle()
                app.registerActivityLifecycleCallbacks(instance)
            }
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //ignored
    }

    override fun onActivityStarted(activity: Activity) {
        //ignored
    }

    override fun onActivityResumed(activity: Activity) {
        isForeground = true
    }

    override fun onActivityPaused(activity: Activity) {
        isForeground = false
    }

    override fun onActivityStopped(activity: Activity) {
        //ignored
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        //ignored
    }

    override fun onActivityDestroyed(activity: Activity) {
        //ignored
    }
}