package com.vegastar.sipapp.component.base

import android.content.Context
import android.text.TextUtils
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError
import com.vegastar.sipapp.MyApplication
import com.vegastar.sipapp.data.DataManager
import com.vegastar.sipapp.data.model.event.AlertEvent
import com.vegastar.sipapp.data.model.event.LoadingEvent
import com.vegastar.sipapp.data.model.event.ProgressEvent
import com.vegastar.sipapp.data.model.event.StatusEvent
import com.vegastar.sipapp.utils.ErrorHandlerUtil
import com.vegastar.sipapp.utils.rx.AppRxHelper
import com.vegastar.sipapp.utils.rx.AppSchedulerProvider
import com.exception.TokenRefreshException
import com.google.gson.Gson
import com.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel(), BaseContract.ViewModel {
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

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    //LiveData
    val progressEvent: MutableLiveData<ProgressEvent> = MutableLiveData<ProgressEvent>()
    val loadingEvent: MutableLiveData<LoadingEvent> = MutableLiveData<LoadingEvent>()
    val alertEvent: MutableLiveData<AlertEvent> = MutableLiveData<AlertEvent>()
    val statusEvent: MutableLiveData<StatusEvent> = MutableLiveData<StatusEvent>()
    val tokenExpiredEvent: MutableLiveData<Boolean> = MutableLiveData()

    init {
        // LiveData
        loadingEvent.value = LoadingEvent.create(0, false)
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
        if (!TextUtils.isEmpty(error)) {
            postToastError(error)
        }
    }

    override fun getErrorString(e: Throwable, vararg args: String): String {
        if (e is TokenRefreshException) {
            printErrorLog("TokenRefreshException")
            handleTokenRefreshException(e)
            return ""
        }

        if (e is ANError) {
            printErrorLog("ANError")
            return errorHandlerUtil.getApiErrorString(e, *args)
        }

        return errorHandlerUtil.getOtherExceptionString(e)
    }

    fun handleTokenRefreshException(e: Throwable?) {
        if (e is TokenRefreshException) handleTokenRefreshException()
    }

    private fun handleTokenRefreshException() {
        //Xóa dữ liệu trong Share Preference
        dataManager.mPreferenceHelper.logout()

        //Show restart app dialog
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