package com.dotanphu.sipapp.component.base

import android.app.Dialog
import android.content.Intent
import android.os.SystemClock
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.component.dialog.AlertDialog
import com.dotanphu.sipapp.component.listener.OnDialogSingleButtonClickListener
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.data.model.event.StatusEvent
import com.dotanphu.sipapp.ui.login.AccountLoginActivity
import com.dotanphu.sipapp.utils.Tool
import com.utils.ProgressDialogUtil
import com.widget.ToastColor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(), BaseContract.View {
    @Inject
    lateinit var mDataManager: DataManager

    private var mProgressDialog: androidx.appcompat.app.AlertDialog? = null

    fun isLiveDataReady(lifecycleOwner: LifecycleOwner): Boolean {
        return lifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED
    }

    /*-----------------------------[ REPLACE FRAGMENT]--------------------------------------------*/
    fun replace(manager: FragmentManager, layout: Int, fragment: Fragment) {
        replace(manager, layout, fragment, true, null)
    }

    fun replace(manager: FragmentManager, layout: Int, fragment: Fragment, hasBackStack: Boolean) {
        replace(manager, layout, fragment, hasBackStack, null)
    }

    fun replace(manager: FragmentManager, layout: Int, fragment: Fragment, hasBackStack: Boolean, tag: String?) {
        replace(manager, layout, fragment, hasBackStack, tag, null)
    }

    fun replace(manager: FragmentManager, layout: Int, fragment: Fragment, sharedElements: Array<Pair<View, String>>?) {
        replace(manager, layout, fragment, true, null, sharedElements)
    }

    fun replace(manager: FragmentManager, layout: Int, fragment: Fragment, hasBackStack: Boolean, tag: String?, sharedElements: Array<Pair<View, String>>?) {
        val transaction = manager.beginTransaction()
        if (!TextUtils.isEmpty(tag)) transaction.replace(layout, fragment, tag) else transaction.replace(layout, fragment)
        if (hasBackStack) transaction.addToBackStack(null)
        if (sharedElements != null && sharedElements.size > 0) {
            for (item in sharedElements) {
                if (item != null) {
                    val view = item.first
                    val transitionName = item.second
                    if (view != null && transitionName != null) transaction.addSharedElement(view, transitionName)
                }
            }
        } else transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
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

    /*-----------------------------[OBSERVE]------------------------------------------------------*/
    override fun registerObserverBaseEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        registerObserverProgressEvent(viewModel, viewLifecycleOwner)
        registerObserverAlertEvent(viewModel, viewLifecycleOwner)
        registerObserverToastEvent(viewModel, viewLifecycleOwner)
        registerObserverTokenExpiredEvent(viewModel, viewLifecycleOwner)
    }

    override fun registerObserverProgressEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        viewModel.progressEvent.observe(viewLifecycleOwner) { progressEvent ->
            if (!TextUtils.isEmpty(progressEvent.content)) updateProgress(progressEvent.content.toString())
            else if (progressEvent.isShow) showProgress()
            else hideProgress()
        }
    }

    override fun registerObserverAlertEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        viewModel.alertEvent.observe(viewLifecycleOwner) { alertEvent ->
            alert(alertEvent.title, alertEvent.content)
        }
    }

    override fun registerObserverToastEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        viewModel.statusEvent.observe(viewLifecycleOwner) { statusEvent -> //LogUtil.wtf("%s %s %s", statusEvent.type, statusEvent.resId, statusEvent.content);
            when (statusEvent.type) {
                StatusEvent.TYPE_SUCCESS -> if (statusEvent.resId != 0) toastSuccess(statusEvent.resId) else toastSuccess(statusEvent.content.toString())
                StatusEvent.TYPE_ERROR -> if (statusEvent.resId != 0) toastError(statusEvent.resId) else toastError(statusEvent.content.toString())
                else -> {}
            }
        }
    }

    override fun registerObserverTokenExpiredEvent(viewModel: BaseViewModel, viewLifecycleOwner: LifecycleOwner) {
        viewModel.tokenExpiredEvent.observe(viewLifecycleOwner) {
            showTokenExpiredDialog()
        }
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
        ToastColor.error(applicationContext, message)
    }

    override fun toastError(resId: Int) {
        try {
            toastError(getString(resId))
        } catch (ignored: Exception) {
            //ignored
        }
    }

    override fun toastSuccess(message: String) {
        ToastColor.success(applicationContext, message)
    }

    override fun toastSuccess(resId: Int) {
        try {
            toastSuccess(getString(resId))
        } catch (ignored: Exception) {
            //ignored
        }
    }

    override fun showTokenExpiredDialog() {
        val dialog: AlertDialog = AlertDialog.newInstance(getString(R.string.error_token_expired), getString(R.string.please_login))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.setOnDialogButtonClickListener(object : OnDialogSingleButtonClickListener {
            override fun onPositiveButtonClick(dialog: Dialog) {
                restartApp()
                dialog.dismiss()
            }
        })
        try {
            dialog.show(supportFragmentManager, null)
        } catch (e: Exception) {
            Toast.makeText(applicationContext, getString(R.string.error_token_expired), Toast.LENGTH_SHORT)
                .show()
            restartApp()
        }
    }

    fun restartApp() {
        val intent = Intent(this, AccountLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun alert(title: String, content: String) {
        val dialog: AlertDialog = AlertDialog.newInstance(title, content)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.setOnDialogButtonClickListener(object : OnDialogSingleButtonClickListener {
            override fun onPositiveButtonClick(dialog: Dialog) {
                dialog.dismiss()
            }
        })
        dialog.show(supportFragmentManager, null)
    }

    override val isNetworkConnected: Boolean
        get() = Tool.isNetworkAvailable(applicationContext)

    override val isLogin: Boolean
        get() = mDataManager.mPreferenceHelper.isLogin

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