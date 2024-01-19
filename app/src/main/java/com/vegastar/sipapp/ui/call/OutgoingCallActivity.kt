package com.vegastar.sipapp.ui.call

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vegastar.sipapp.R
import com.vegastar.sipapp.component.base.BaseActivity
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_DISPLAY_NAME
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_FCM_TOKEN
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_PHONE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OutgoingCallActivity : BaseActivity() {
    companion object {
        fun newIntent(context: Context, phone: String?, displayName: String?, fcmToken: String?): Intent {
            val intent = Intent(context, OutgoingCallActivity::class.java)
            intent.putExtra(KEY_PHONE, phone)
            intent.putExtra(KEY_DISPLAY_NAME, displayName)
            intent.putExtra(KEY_FCM_TOKEN, fcmToken)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        val intent = getIntent()
        val phone = intent.getStringExtra(KEY_PHONE)
        val displayName = intent.getStringExtra(KEY_DISPLAY_NAME)
        val fcmToken = intent.getStringExtra(KEY_FCM_TOKEN)
        replace(supportFragmentManager, R.id.content, OutgoingCallFragment.newInstance(phone, displayName, fcmToken), false)
    }
}