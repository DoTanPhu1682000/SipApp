package com.dotanphu.sipapp.ui.call

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.component.base.BaseActivity
import com.dotanphu.sipapp.utils.constant.KeyConstant.KEY_PHONE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OutgoingCallActivity : BaseActivity() {
    companion object {
        fun newIntent(context: Context, phone: String): Intent {
            val intent = Intent(context, OutgoingCallActivity::class.java)
            intent.putExtra(KEY_PHONE, phone)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        val intent = getIntent()
        val phone = intent.getStringExtra(KEY_PHONE)
        replace(supportFragmentManager, R.id.content, OutgoingCallFragment.newInstance(phone), false)
    }
}