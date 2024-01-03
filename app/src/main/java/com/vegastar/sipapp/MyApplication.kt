package com.vegastar.sipapp

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.vegastar.sipapp.utils.ActivityLifecycle
import com.vegastar.sipapp.utils.NotificationUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.utils.LogUtil
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class MyApplication : Application() {
    var mInstance: MyApplication? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var getInstance: MyApplication

        @SuppressLint("StaticFieldLeak")
        lateinit var getContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        init()
        test()
    }

    private fun test() {
        //code for test
    }

    private fun init() {
        initFont()
        initOkHttpClient()
        initFirebase()
        initDefaultNotificationChannel()

        //Detector Foreground or Background
        ActivityLifecycle.init(this)

        //Log
        LogUtil.init(AppConfig.TAG, true)
    }

    private fun initOkHttpClient() {
        //Log Http: Chỉ sử dụng 1 trong 2 loại Log Http bên dưới cho AndroidNetworking.
        //OkHttp log
//        val httpLoggingNormal = HttpLoggingInterceptor(object : Logger() {
//            fun log(message: String?) {
//                Log.e(TAG, message)
//            }
//        })
//        httpLoggingNormal.setLevel(HttpLoggingInterceptor.Level.BODY)

        //Ihsanbal log
        val httpLoggingBeauty = LoggingInterceptor.Builder()
            .loggable(true)
            .setLevel(Level.BODY) //BODY
            .log(Platform.WARN)
            .tag("NET") //.executor(Executors.newSingleThreadExecutor())
            .build()

        //AndroidNetworking
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .writeTimeout(12, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingBeauty) //.addInterceptor(httpLoggingNormal)
            .build()
        AndroidNetworking.initialize(applicationContext, client)
    }

    private fun initFont() {
        //Font
        ViewPump.builder()
            .addInterceptor(CalligraphyInterceptor(CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.path_font_normal))
                .setFontAttrId(io.github.inflationx.calligraphy3.R.attr.fontPath)
                .build()
            )
            )
    }

    private fun initFirebase() {
        //FireBase
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FirebaseMessaging", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            LogUtil.wtf(token)
        })
    }

    private fun initDefaultNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        NotificationUtil.createChannelTypeNotification(applicationContext, notificationManager)
        NotificationUtil.createChannelPrepareIncomingCallNotification(applicationContext, notificationManager)
        NotificationUtil.createChannelIncomingCallNotification(applicationContext, notificationManager)
        NotificationUtil.createChannelReminderNotification(applicationContext, notificationManager)
    }
}