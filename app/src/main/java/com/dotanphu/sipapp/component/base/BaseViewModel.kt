package com.dotanphu.sipapp.component.base

import android.content.Context
import android.text.TextUtils
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError
import com.dotanphu.sipapp.MyApplication
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.data.model.event.AlertEvent
import com.dotanphu.sipapp.data.model.event.LoadingEvent
import com.dotanphu.sipapp.data.model.event.ProgressEvent
import com.dotanphu.sipapp.data.model.event.StatusEvent
import com.dotanphu.sipapp.utils.ErrorHandlerUtil
import com.dotanphu.sipapp.utils.rx.AppRxHelper
import com.dotanphu.sipapp.utils.rx.AppSchedulerProvider
import com.exception.TokenRefreshException
import com.google.gson.Gson
import com.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel(), BaseContract.ViewModel {
    protected val compositeDisposable: CompositeDisposable

    @Inject
    lateinit var appRxHelper: AppRxHelper

    @Inject
    lateinit var dataManager: DataManager

    @Inject
    lateinit var schedulerProvider: AppSchedulerProvider

    @Inject
    lateinit var errorHandlerUtil: ErrorHandlerUtil

    @Inject
    lateinit var gson: Gson

    //LiveData
    val progressEvent: MutableLiveData<ProgressEvent>
    val loadingEvent: MutableLiveData<LoadingEvent>
    val alertEvent: MutableLiveData<AlertEvent>
    val statusEvent: MutableLiveData<StatusEvent>
    val tokenExpiredEvent: MutableLiveData<Boolean>

    init {
        compositeDisposable = CompositeDisposable()

        //LiveData
        loadingEvent = MutableLiveData<LoadingEvent>()
        progressEvent = MutableLiveData<ProgressEvent>()
        alertEvent = MutableLiveData<AlertEvent>()
        statusEvent = MutableLiveData<StatusEvent>()
        tokenExpiredEvent = MutableLiveData()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    /*-----------------------------[POST EVENT]---------------------------------------------------*/
    override fun postShowProgress() {
        progressEvent.postValue(ProgressEvent.create(true))
    }

    override fun postUpdateProgress(text: String) {
        progressEvent.postValue(ProgressEvent.create(text))
    }

    override fun postHideProgress() {
        progressEvent.postValue(ProgressEvent.create(false))
    }

    override fun postShowLoading(requestCode: Int) {
        loadingEvent.postValue(LoadingEvent.create(requestCode, true))
    }

    override fun postHideLoading(requestCode: Int) {
        loadingEvent.postValue(LoadingEvent.create(requestCode, false))
    }

    override fun postAlert(title: String, content: String) {
        alertEvent.postValue(AlertEvent.create(title, content))
    }

    override fun postToastError(content: String) {
        statusEvent.postValue(StatusEvent.create(StatusEvent.TYPE_ERROR, content))
    }

    override fun postToastError(@StringRes resId: Int) {
        statusEvent.postValue(StatusEvent.create(StatusEvent.TYPE_ERROR, resId))
    }

    override fun postToastSuccess(content: String) {
        statusEvent.postValue(StatusEvent.create(StatusEvent.TYPE_SUCCESS, content))
    }

    override fun postToastSuccess(resId: Int) {
        statusEvent.postValue(StatusEvent.create(StatusEvent.TYPE_SUCCESS, resId))
    }

    override fun postShowTokenExpiredDialog() {
        tokenExpiredEvent.postValue(true)
    }

    /*-----------------------------[HANDLE ERROR]-------------------------------------------------*/
    override fun handleError(e: Throwable, vararg args: String) {
        e.printStackTrace()
        val error = getErrorString(e, *args)
        if (!TextUtils.isEmpty(error)) postToastError(error)
    }

    override fun getErrorString(e: Throwable, vararg args: String): String {
        if (e is TokenRefreshException) {
            printErrorLog("TokenRefreshException")
            handleTokenRefreshException()
            return ""
        }
        if (e is ANError) {
            printErrorLog("ANError")
            return errorHandlerUtil.getApiErrorString(e, args.toString())
        }
        return errorHandlerUtil.getOtherExceptionString(e)
    }

    fun handleTokenRefreshException(e: Throwable?) {
        if (e is TokenRefreshException) handleTokenRefreshException()
    }

    private fun handleTokenRefreshException() {
        //Xóa dữ liệu trong Share Preference
        dataManager.mPreferenceHelper.logout()

        //Show  restart app dialog
        postShowTokenExpiredDialog()
    }

    private fun printErrorLog(s: String) {
        LogUtil.clearLogAdapters()
        LogUtil.init("APP", true, 3)
        LogUtil.e(s)
        LogUtil.clearLogAdapters()
        LogUtil.init("APP", true)
    }

    fun getBaseContext(): Context {
        return MyApplication.getInstance.applicationContext
    }
}