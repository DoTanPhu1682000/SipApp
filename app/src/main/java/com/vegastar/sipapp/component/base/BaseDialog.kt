package com.vegastar.sipapp.component.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.vegastar.sipapp.AppConfig.MIN_CLICK_INTERVAL
import com.vegastar.sipapp.R
import com.vegastar.sipapp.utils.Helper
import com.vegastar.sipapp.utils.Tool

open class BaseDialog : DialogFragment(), BaseContract.View {
    private var mActivity: BaseActivity? = null

    private var isCanceledOnTouchOutside = true
    private val isFullScreenDialog = false

    fun isLiveDataReady(lifecycleOwner: LifecycleOwner): Boolean {
        return lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED
    }

    /*-----------------------------[ METHOD]------------------------------------------------------*/
    fun setCanceledOnTouchOutside(isCanceledOnTouchOutside: Boolean) {
        this.isCanceledOnTouchOutside = isCanceledOnTouchOutside
    }

    open fun getBaseActivity(): BaseActivity? {
        return mActivity
    }

    open fun getBaseContext(): Context? {
        return if (context != null) context else mActivity?.applicationContext
    }

    open fun checkLiveDataState(lifecycleOwner: LifecycleOwner): Boolean {
        return lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null && dialog.window != null) {
            val window = dialog.window
            dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside)
            if (isFullScreenDialog) {
                window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            } else {
                var width: Int = Tool.getScreenWidth(requireActivity(), 0)
                if (width > 0) {
                    window!!.setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext()!!, R.drawable.bg_white_round))
                    val padding = resources.getDimension(R.dimen.sizeStandard).toInt()
                    width = if (Helper.isTablet(requireContext())) width / 3 * 2 else width - padding * 2
                    window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
                }
            }
        }
    }

    /*--------------------------------------[SINGLE CLICK]----------------------------------------*/
    private var mLastClickTime: Long = 0

    open fun isClickable(): Boolean {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
        mLastClickTime = currentClickTime
        return elapsedTime > MIN_CLICK_INTERVAL
    }

    /*-----------------------------[ OVERRIDE ]---------------------------------------------------*/
    override fun showLoginDialog() {
        mActivity?.showLoginDialog()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            mActivity = context
        }
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    override fun showProgress() {
        if (mActivity != null) {
            mActivity?.showProgress()
        }
    }

    override fun showProgress(text: String, setCancelable: Boolean, setCanceledOnTouchOutside: Boolean) {
        if (mActivity != null) {
            mActivity?.showProgress(text, setCancelable, setCanceledOnTouchOutside)
        }
    }

    override fun updateProgress(text: String) {
        if (mActivity != null) {
            mActivity?.updateProgress(text)
        }
    }

    override fun hideProgress() {
        if (mActivity != null) {
            mActivity?.hideProgress()
        }
    }

    override fun showTokenExpiredDialog() {
        mActivity?.showTokenExpiredDialog()
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

    override fun alert(title: String, content: String) {
        mActivity?.alert(title, content)
    }

    override val isNetworkConnected: Boolean
        get() = Tool.isNetworkAvailable(requireContext())

    override val isLogin: Boolean
        get() = mActivity?.isLogin ?: false

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