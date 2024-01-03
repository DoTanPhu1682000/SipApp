package com.vegastar.sipapp.ui.dialer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vegastar.sipapp.R
import com.vegastar.sipapp.component.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialerActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context?): Intent {
            return Intent(context, DialerActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        replace(supportFragmentManager, R.id.content, DialerFragment.newInstance(), false)
    }
}