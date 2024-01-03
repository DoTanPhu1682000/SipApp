package com.vegastar.sipapp.component.base

import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner

interface BaseContract {
    interface View {
        /*-----------------------------[METHOD]---------------------------------------------------*/
        fun showLoginDialog()
        fun showProgress()
        fun showProgress(text: String, setCancelable: Boolean, setCanceledOnTouchOutside: Boolean)
        fun updateProgress(text: String)
        fun hideProgress()
        fun toastError(message: String)
        fun toastError(@StringRes resId: Int)
        fun toastSuccess(message: String)
        fun toastSuccess(@StringRes resId: Int)
        fun showTokenExpiredDialog()
        fun alert(title: String, content: String)
        val isNetworkConnected: Boolean
        val isLogin: Boolean

        /*-----------------------------[OBSERVE]--------------------------------------------------*/
        fun registerObserverBaseEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner)
        fun registerObserverProgressEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner)
        fun registerObserverTokenExpiredEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner)
        fun registerObserverAlertEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner)
        fun registerObserverToastEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner)
    }

    interface ViewModel {
        /*-----------------------------[POST EVENT]-----------------------------------------------*/
        fun postShowProgress()
        fun postUpdateProgress(text: String)
        fun postHideProgress()
        fun postShowLoading(requestCode: Int)
        fun postHideLoading(requestCode: Int)
        fun postToastError(content: String)
        fun postToastError(@StringRes resId: Int)
        fun postToastSuccess(content: String)
        fun postToastSuccess(@StringRes resId: Int)
        fun postAlert(title: String, content: String)
        fun postShowTokenExpiredDialog()

        /*-----------------------------[HANDLE ERROR]---------------------------------------------*/
        fun handleError(e: Throwable, vararg args: String)
        fun getErrorString(e: Throwable, vararg args: String): String
    }
}