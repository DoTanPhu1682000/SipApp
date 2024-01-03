package com.dotanphu.sipapp.ui.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.custom.ViewPager2Adapter
import com.dotanphu.sipapp.AppConfig.BACK_TO_EXIT_TIME
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.component.base.BaseActivity
import com.dotanphu.sipapp.component.dialog.ConfirmDialog
import com.dotanphu.sipapp.component.listener.OnDialogButtonClickListener
import com.dotanphu.sipapp.databinding.ActivityMainBinding
import com.dotanphu.sipapp.ui.contact.ContactFragment
import com.dotanphu.sipapp.ui.dialer.DialerFragment
import com.dotanphu.sipapp.ui.history.CallLogFragment
import com.dotanphu.sipapp.utils.PermissionsHelper
import com.dotanphu.sipapp.utils.Tool
import com.dotanphu.sipapp.utils.constant.RequestCode.REQUEST_DRAW_OVERLAY_SETTING
import com.dotanphu.sipapp.utils.constant.RequestCode.REQUEST_ENABLE_LOCATION
import com.google.android.material.navigation.NavigationBarView
import com.utils.LogUtil
import com.widget.ToastColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context?): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ViewPager2Adapter

    private var doubleBackToExitPressedOnce = false

    private val mOnItemSelectedListener = NavigationBarView.OnItemSelectedListener { item: MenuItem ->
        val itemId = item.itemId
        if (itemId == R.id.navRecently) {
            binding.viewPager.setCurrentItem(0, false)
            return@OnItemSelectedListener true
        } else if (itemId == R.id.navPhonebook) {
            binding.viewPager.setCurrentItem(1, false)
            return@OnItemSelectedListener true
        } else if (itemId == R.id.navKeyboard) {
            binding.viewPager.setCurrentItem(2, false)
            return@OnItemSelectedListener true
        } else if (itemId == R.id.navFavourite) {
            binding.viewPager.setCurrentItem(3, false)
            return@OnItemSelectedListener true
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        listener()
    }

    private fun initData() {
        binding.viewPager.isUserInputEnabled = false
        binding.navigation.setOnItemSelectedListener(mOnItemSelectedListener)

        binding.navigation.itemIconTintList = null

        //Chú ý: Không sử dụng ButterKnife trong các Fragment của ViewPager
        adapter = ViewPager2Adapter(this)

        adapter.addFragment(CallLogFragment.newInstance())
        adapter.addFragment(ContactFragment.newInstance())
        adapter.addFragment(DialerFragment.newInstance())
        adapter.addFragment(DialerFragment.newInstance())
        binding.viewPager.adapter = adapter

        // We will need the RECORD_AUDIO permission for video call
        val permissionsHelper = PermissionsHelper(this)

        permissionsHelper.requestAllPermissions(onPermissionGranted = {
            // Xử lý khi tất cả quyền đã được cấp
            LogUtil.wtf("tất cả quyền đã được cấp")
            showOverlayDialog()
        }, onPermissionDenied = {
            // Xử lý khi một hoặc nhiều quyền đã bị từ chối
            LogUtil.wtf("một hoặc nhiều quyền đã bị từ chối")
        })
    }

    private fun listener() {
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.navigation.selectedItemId = R.id.navRecently
                    1 -> binding.navigation.selectedItemId = R.id.navPhonebook
                    2 -> binding.navigation.selectedItemId = R.id.navKeyboard
                    3 -> binding.navigation.selectedItemId = R.id.navFavourite
                }
            }
        })
    }

    private fun showOverlayDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val d: ConfirmDialog = ConfirmDialog.newInstance(R.string.notification, R.string.msg_permission_display_over_other_apps)
            d.setCanceledOnTouchOutside(false)
            d.isCancelable = false
            d.setOnDialogButtonClickListener(object : OnDialogButtonClickListener {
                override fun onPositiveButtonClick(dialog: Dialog?) {
                    startActivityForResult(Tool.getSettingManageOverlayPermissionIntent(applicationContext), REQUEST_DRAW_OVERLAY_SETTING)
                    dialog?.cancel()
                }

                override fun onNegativeButtonClick(dialog: Dialog?) {
                    showLocationDialog()
                    dialog?.cancel()
                }
            })
            try {
                d.show(supportFragmentManager, null)
            } catch (e: Exception) {
                LogUtil.e(e.message)
            }
        }
    }

    private fun showLocationDialog() {
        val isLocationEnabled: Boolean = Tool.isLocationEnabled(baseContext)
        if (isLocationEnabled) showXiaomiNotiSettingDialog() else Tool.displayLocationSettingsRequest(this, REQUEST_ENABLE_LOCATION)
    }

    private fun showXiaomiNotiSettingDialog() {
        //
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_DRAW_OVERLAY_SETTING -> showLocationDialog()
        }
    }

    override fun onBackPressed() {
        //Checking for fragment count on backstack
        if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStack() else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true
            ToastColor.info(applicationContext, getString(R.string.message_exit_app))
            Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, BACK_TO_EXIT_TIME)
        } else {
            super.onBackPressed()
        }
    }
}