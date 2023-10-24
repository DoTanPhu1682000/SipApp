package com.dotanphu.sipapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.utils.LogUtil
import dagger.hilt.android.HiltAndroidApp
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
        initFirebase()
        test()
    }

    private fun test() {
        //code for test
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
            Log.e("FirebaseMessaging", token)
        })
    }

    private fun init() {
        initOkHttpClient()

        //Log
        LogUtil.init("APP", true)
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
}