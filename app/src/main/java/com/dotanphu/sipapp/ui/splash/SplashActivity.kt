package com.dotanphu.sipapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dotanphu.sipapp.component.base.BaseActivity
import com.dotanphu.sipapp.databinding.ActivitySplashBinding
import com.dotanphu.sipapp.ui.login.AccountLoginActivity
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
            val intent = Intent(this, AccountLoginActivity::class.java)
            startActivity(intent)
        }, 1000)
    }
}