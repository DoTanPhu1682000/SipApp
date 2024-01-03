package com.dotanphu.sipapp.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dotanphu.sipapp.AppConfig.REMOVE_USER_DATA_WHEN_APP_START
import com.dotanphu.sipapp.component.base.BaseActivity
import com.dotanphu.sipapp.databinding.ActivitySplashBinding
import com.dotanphu.sipapp.ui.home.MainActivity
import com.dotanphu.sipapp.ui.login.AccountLoginActivity
import com.dotanphu.sipapp.utils.core.CoreHelper
import com.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startAccountLoginActivity()
    }

    private fun startAccountLoginActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (REMOVE_USER_DATA_WHEN_APP_START) {
                //Xóa dữ liệu trong Share Preference
                mDataManager.mPreferenceHelper.logout()

                startActivity(AccountLoginActivity.newIntent(applicationContext))
            } else {
                if (isLogin) {
                    if (CoreHelper.getInstance(applicationContext)?.isCoreRunning() == false) {
                        CoreHelper.getInstance(applicationContext)?.login()
                    }
                    startActivity(MainActivity.newIntent(applicationContext))
                    finish()
                } else {
                    startActivity(AccountLoginActivity.newIntent(applicationContext))
                    finish()
                }
            }
        }, 1000)
    }
}