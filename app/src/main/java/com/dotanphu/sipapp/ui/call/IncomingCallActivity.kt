package com.dotanphu.sipapp.ui.call

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.component.base.BaseActivity
import com.dotanphu.sipapp.ui.dialer.DialerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingCallActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, IncomingCallActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        replace(supportFragmentManager, R.id.content, IncomingCallFragment.newInstance(), false)
    }
}