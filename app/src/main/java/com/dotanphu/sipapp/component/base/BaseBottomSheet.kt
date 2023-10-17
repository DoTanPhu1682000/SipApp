package com.dotanphu.sipapp.component.base

import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BaseBottomSheet: BottomSheetDialogFragment(),BaseContract.View {
    private val mActivity: BaseActivity? = null
    private val mProgressDialog: AlertDialog? = null
    private val requestKeyForResult: String? = null

    override fun showLoginDialog() {
        mActivity?.showLoginDialog()
    }

    override fun showProgress() {
        //ignore
    }

    override fun showProgress(text: String, setCancelable: Boolean, setCanceledOnTouchOutside: Boolean) {
        //ignore
    }

    override fun updateProgress(text: String) {
        //ignore
    }

    override fun hideProgress() {
        //ignore
    }

    override fun toastError(message: String) {
        mActivity?.toastError(message)
    }

    override fun toastError(resId: Int) {
        mActivity?.toastError(resId)
    }

    override fun toastSuccess(message: String) {
        mActivity?.toastSuccess(message)
    }

    override fun toastSuccess(resId: Int) {
        mActivity?.toastSuccess(resId)
    }

    override fun showTokenExpiredDialog() {
        mActivity?.showTokenExpiredDialog()
    }

    override fun alert(title: String, content: String) {
        mActivity?.alert(title, content)
    }

    override val isNetworkConnected: Boolean
        get() = true

    override val isLogin: Boolean
        get() = true

    override fun registerObserverBaseEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        mActivity?.registerObserverBaseEvent(viewModel, viewLifecycleOwner)
    }

    override fun registerObserverProgressEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        mActivity?.registerObserverProgressEvent(viewModel, viewLifecycleOwner)
    }

    override fun registerObserverTokenExpiredEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        mActivity?.registerObserverAlertEvent(viewModel, viewLifecycleOwner)
    }

    override fun registerObserverAlertEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        mActivity?.registerObserverToastEvent(viewModel, viewLifecycleOwner)
    }

    override fun registerObserverToastEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        mActivity?.registerObserverTokenExpiredEvent(viewModel, viewLifecycleOwner)
    }
}