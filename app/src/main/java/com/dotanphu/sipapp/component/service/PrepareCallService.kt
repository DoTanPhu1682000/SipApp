package com.dotanphu.sipapp.component.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.data.model.response.Login
import com.dotanphu.sipapp.utils.NotificationUtil
import com.dotanphu.sipapp.utils.NotificationUtil.NOTIFY_ID_PREPARE_CALL
import com.dotanphu.sipapp.utils.core.CoreHelper
import com.dotanphu.sipapp.utils.core.CoreHelperListener
import com.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@AndroidEntryPoint
class PrepareCallService : Service() {
    @Inject
    lateinit var mDataManager: DataManager

    private var mCompositeDisposable: CompositeDisposable? = null

    companion object {
        fun start(context: Context) {
            LogUtil.wtf("LoginService.start")
            val intent = Intent(context, PrepareCallService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            LogUtil.wtf("LoginService.stop")
            val serviceIntent = Intent(context, PrepareCallService::class.java)
            context.stopService(serviceIntent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        LogUtil.i("onCreate")

        mCompositeDisposable = CompositeDisposable()
        val notification = NotificationUtil.createPrepareIncomingCallNotification(applicationContext)

        //Start Foreground Service
        startForeground(NOTIFY_ID_PREPARE_CALL, notification)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        LogUtil.i("onStartCommand")

        if (!mDataManager.mPreferenceHelper.isLogin) {
            stopSelf()
        }

        login()
        return START_NOT_STICKY
    }

    fun login() {
        val username = mDataManager.mPreferenceHelper.username.toString()
        val password = mDataManager.mPreferenceHelper.password.toString()
        val d: Disposable = mDataManager.mApiHelper.login(username, password)
            .doOnSuccess {
                mDataManager.mPreferenceHelper.saveLoginInfo(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : DisposableSingleObserver<Login>(), CoreHelperListener {
                override fun onSuccess(login: Login) {
                    CoreHelper.getInstance(applicationContext)?.login()
                    CoreHelper.getInstance(applicationContext)?.listener = this
                }

                override fun onError(e: Throwable) {}
                override fun onRegistrationStateChanged(isSuccessful: Boolean) {
                    if (isSuccessful) {
                        NotificationUtil.createIncomingCallNotification(applicationContext)
                    }
                }
            })
        mCompositeDisposable?.add(d)
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.wtf("onDestroy")
        if (mCompositeDisposable != null) mCompositeDisposable!!.dispose()

        stopForeground(true)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}