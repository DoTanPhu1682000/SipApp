package com.vegastar.sipapp.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.vegastar.sipapp.AppConfig.REMOVE_USER_DATA_WHEN_APP_START
import com.vegastar.sipapp.component.base.BaseActivity
import com.vegastar.sipapp.databinding.ActivitySplashBinding
import com.vegastar.sipapp.ui.home.MainActivity
import com.vegastar.sipapp.ui.login.AccountLoginActivity
import com.vegastar.sipapp.utils.core.CoreHelper
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
                        CoreHelper.getInstance(applicationContext)?.start()
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