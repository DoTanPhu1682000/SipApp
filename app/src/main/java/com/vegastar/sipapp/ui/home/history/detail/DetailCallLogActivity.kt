package com.vegastar.sipapp.ui.home.history.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vegastar.sipapp.R
import com.vegastar.sipapp.component.base.BaseActivity
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_DISPLAY_NAME
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_LAST_CALL_LOG_START_TIME
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_USER_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCallLogActivity : BaseActivity() {

    companion object {
        fun newIntent(context: Context, username: String, displayName: String, lastCallLogStartTimestamp: String): Intent {
            val intent = Intent(context, DetailCallLogActivity::class.java)
            intent.putExtra(KEY_USER_NAME, username)
            intent.putExtra(KEY_DISPLAY_NAME, displayName)
            intent.putExtra(KEY_LAST_CALL_LOG_START_TIME, lastCallLogStartTimestamp)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        val intent = intent
        val username = intent.getStringExtra(KEY_USER_NAME)
        val displayName = intent.getStringExtra(KEY_DISPLAY_NAME)
        val lastCallLogStartTimestamp = intent.getStringExtra(KEY_LAST_CALL_LOG_START_TIME)
        replace(supportFragmentManager, R.id.content, DetailCallLogFragment.newInstance(username, displayName, lastCallLogStartTimestamp), false)
    }
}