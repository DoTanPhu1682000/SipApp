package com.vegastar.sipapp.ui.call

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vegastar.sipapp.R
import com.vegastar.sipapp.component.base.BaseActivity
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_CALL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingCallActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, IncomingCallActivity::class.java)
        }

        fun newIntent(context: Context, checkAcceptCall: Boolean): Intent {
            val intent = Intent(context, IncomingCallActivity::class.java)
            intent.putExtra(KEY_CALL, checkAcceptCall)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        val bundle = intent.extras
        var checkAcceptCall: Boolean = false

        if (bundle == null) {
            replace(supportFragmentManager, R.id.content, IncomingCallFragment.newInstance(), false)
            return
        }

        checkAcceptCall = bundle.getBoolean(KEY_CALL)
        if (checkAcceptCall) {
            replace(supportFragmentManager, R.id.content, IncomingCallFragment.newNotificationInstance(checkAcceptCall), false)
        }
    }
}