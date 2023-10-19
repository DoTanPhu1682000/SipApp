package com.dotanphu.sipapp.component.base

import android.os.SystemClock
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.dotanphu.sipapp.AppConfig.MIN_CLICK_INTERVAL
import com.dotanphu.sipapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseFragment : Fragment(), BaseContract.View {
    private val mActivity: BaseActivity? = null
    private val mProgressDialog: AlertDialog? = null
    private val requestKeyForResult: String? = null

    /*-----------------------------[ REPLACE FRAGMENT]--------------------------------------------*/
    fun replace(manager: FragmentManager, layout: Int, fragment: Fragment) {
        replace(manager, layout, fragment, true, null)
    }

    fun replace(manager: FragmentManager, layout: Int, fragment: Fragment, hasBackStack: Boolean) {
        replace(manager, layout, fragment, hasBackStack, null)
    }

    fun replace(manager: FragmentManager, layout: Int, fragment: Fragment, hasBackStack: Boolean, tag: String?) {
        val transaction = manager.beginTransaction()
        transaction.replace(layout, fragment, tag)
        if (hasBackStack) transaction.addToBackStack(null)
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        transaction.commitAllowingStateLoss()
    }

    fun add(manager: FragmentManager, layout: Int, fragment: Fragment) {
        add(manager, layout, fragment, true, true)
    }

    fun add(manager: FragmentManager, layout: Int, fragment: Fragment, hasBackStack: Boolean) {
        add(manager, layout, fragment, hasBackStack, true)
    }

    fun add(manager: FragmentManager, layout: Int, fragment: Fragment, hasBackStack: Boolean, anim: Boolean) {
        val transaction = manager.beginTransaction()
        if (anim) transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        transaction.add(layout, fragment)
        if (hasBackStack) transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }

    /*--------------------------------------[SINGLE CLICK]----------------------------------------*/
    private var mLastClickTime: Long = 0

    fun isClickable(): Boolean {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
        mLastClickTime = currentClickTime
        return elapsedTime > MIN_CLICK_INTERVAL
    }

    fun checkLiveDataState(lifecycleOwner: LifecycleOwner): Boolean {
        return lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED
    }

    fun isLiveDataReady(lifecycleOwner: LifecycleOwner): Boolean {
        return lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED
    }

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
        get() = mActivity!!.isNetworkConnected

    override val isLogin: Boolean
        get() = mActivity!!.isLogin

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