package com.dotanphu.sipapp.ui.login

import android.os.Bundle
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.component.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountLoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        replace(supportFragmentManager, R.id.content, AccountLoginFragment.newInstance(), false)
    }
}