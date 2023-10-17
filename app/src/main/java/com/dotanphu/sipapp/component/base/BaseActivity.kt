package com.dotanphu.sipapp.component.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.data.event.StatusEvent
import com.google.android.material.button.MaterialButton
import com.utils.LogUtil
import com.utils.ProgressDialogUtil
import com.widget.ToastColor
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(), BaseContract.View {
    @Inject
    lateinit var mDataManager: DataManager

    private var mProgressDialog: AlertDialog? = null

    fun isLiveDataReady(lifecycleOwner: LifecycleOwner): Boolean {
        return lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED
    }

    /*-----------------------------[ REPLACE FRAGMENT]--------------------------------------------*/
    protected fun replace(manager: FragmentManager?, layout: Int, fragment: Fragment?, hasBackStack: Boolean, tag: String? = null, sharedElements: Array<Pair<View?, String?>?>? = null) {
        if (manager == null || fragment == null) return
        val transaction = manager.beginTransaction()
        transaction.replace(layout, fragment, tag)
        if (hasBackStack) transaction.addToBackStack(null)
        if (sharedElements != null && sharedElements.size > 0) {
            for (item in sharedElements) {
                if (item != null) {
                    val view = item.first
                    val transitionName = item.second
                    if (view != null && transitionName != null) transaction.addSharedElement(view, transitionName)
                }
            }
        }
        //else
        //    transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.commitAllowingStateLoss()
    }

    /*-----------------------------[OBSERVE]------------------------------------------------------*/
    override fun registerObserverBaseEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        registerObserverProgressEvent(viewModel, viewLifecycleOwner)
        registerObserverAlertEvent(viewModel, viewLifecycleOwner)
        registerObserverToastEvent(viewModel, viewLifecycleOwner)
        registerObserverTokenExpiredEvent(viewModel, viewLifecycleOwner)
    }

    override fun registerObserverProgressEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        viewModel.progressEvent.observe(viewLifecycleOwner) { progressEvent -> if (!TextUtils.isEmpty(progressEvent.content)) updateProgress(progressEvent.content.toString()) else if (progressEvent.isShow) showProgress() else hideProgress() }
    }

    override fun registerObserverAlertEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        viewModel.alertEvent.observe(viewLifecycleOwner) { alertEvent -> alert(alertEvent.title, alertEvent.content) }
    }

    override fun registerObserverToastEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        viewModel.statusEvent.observe(viewLifecycleOwner) { statusEvent -> //LogUtil.wtf("%s %s %s", statusEvent.type, statusEvent.resId, statusEvent.content);
            when (statusEvent.type) {
                StatusEvent.TYPE_SUCCESS -> if (statusEvent.resId !== 0) toastSuccess(statusEvent.resId) else toastSuccess(statusEvent.content.toString())
                StatusEvent.TYPE_ERROR -> if (statusEvent.resId !== 0) toastError(statusEvent.resId) else toastError(statusEvent.content.toString())
                else -> {}
            }
        }
    }

    override fun registerObserverTokenExpiredEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        viewModel.tokenExpiredEvent.observe(viewLifecycleOwner) { showTokenExpiredDialog() }
    }

    /*-----------------------------[METHOD]-------------------------------------------------------*/
    override fun showLoginDialog() {}
    override fun showProgress() {
        ProgressDialogUtil.hideProgressDialog(mProgressDialog)
        mProgressDialog = ProgressDialogUtil.showProgressDialog(this)
    }

    override fun showProgress(text: String, setCancelable: Boolean, setCanceledOnTouchOutside: Boolean) {
        ProgressDialogUtil.hideProgressDialog(mProgressDialog)
        mProgressDialog = ProgressDialogUtil.showProgressDialog(this, text, setCancelable, setCanceledOnTouchOutside)
    }

    override fun updateProgress(text: String) {
        ProgressDialogUtil.updateProgressDialog(mProgressDialog, text)
    }

    override fun hideProgress() {
        ProgressDialogUtil.hideProgressDialog(mProgressDialog)
    }

    override fun toastError(message: String) {
        if (message != null) ToastColor.error(applicationContext, message)
    }

    override fun toastError(resId: Int) {
        try {
            toastError(getString(resId))
        } catch (ignored: Exception) {
            //ignored
        }
    }

    override fun toastSuccess(message: String) {
        if (message != null) ToastColor.success(applicationContext, message)
    }

    override fun toastSuccess(resId: Int) {
        try {
            toastSuccess(getString(resId))
        } catch (ignored: Exception) {
            //ignored
        }
    }

    override fun showTokenExpiredDialog() {
//        val dialog: AlertDialog = AlertDialog.newInstance(getString(R.string.error_token_expired), getString(R.string.please_login))
//        dialog.setCanceledOnTouchOutside(false)
//        dialog.setCancelable(false)
//        dialog.setOnDialogButtonClickListener(object : OnDialogSingleButtonClickListener() {
//            fun onPositiveButtonClick(dialog: Dialog) {
//                restartApp()
//                dialog.dismiss()
//            }
//        })
//        try {
//            dialog.show(supportFragmentManager, null)
//        } catch (e: Exception) {
//            Toast.makeText(applicationContext, getString(R.string.error_token_expired), Toast.LENGTH_SHORT)
//                .show()
//            restartApp()
//        }
    }

    override fun alert(title: String, content: String) {
//        val dialog: AlertDialog = AlertDialog.newInstance(title, content)
//        dialog.setCanceledOnTouchOutside(true)
//        dialog.setCancelable(true)
//        dialog.setOnDialogButtonClickListener(object : OnDialogSingleButtonClickListener() {
//            fun onPositiveButtonClick(dialog: Dialog) {
//                dialog.dismiss()
//            }
//        })
//        dialog.show(supportFragmentManager, null)
    }

    override val isNetworkConnected: Boolean
        //        get() = Tool.isNetworkAvailable(applicationContext)
        get() = true
    override val isLogin: Boolean
        //        get() = mDataManager.getPreference().isLogin()
        get() = true

    companion object {
        /*--------------------------------------[SINGLE CLICK]----------------------------------------*/
        private var mLastClickTime: Long = 0
        val isClickable: Boolean
            get() {
                val currentClickTime = SystemClock.uptimeMillis()
                val elapsedTime = currentClickTime - mLastClickTime
                mLastClickTime = currentClickTime
                return elapsedTime > 700
            }
    }
}