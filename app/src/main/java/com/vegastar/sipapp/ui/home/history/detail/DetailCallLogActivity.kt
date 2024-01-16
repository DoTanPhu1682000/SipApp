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
        fun newIntent(context: Context, username: String?, displayName: String?, lastCallLogStartTimestamp: Long): Intent {
            val intent = Intent(context, DetailCallLogActivity::class.java)
            intent.putExtra(KEY_USER_NAME, username)
            intent.putExtra(KEY_DISPLAY_NAME, displayName)
            intent.putExtra(KEY_LAST_CALL_LOG_START_TIME, lastCallLogStartTimestamp)
            return intent
        }
    }

    private var username: String? = null
    private var displayName: String? = null
    private var lastCallLogStartTimestamp: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        val bundle = intent.extras
        if (bundle != null) {
            username = bundle.getString(KEY_USER_NAME)
            displayName = bundle.getString(KEY_DISPLAY_NAME)
            lastCallLogStartTimestamp = bundle.getLong(KEY_LAST_CALL_LOG_START_TIME)
        }
        replace(supportFragmentManager, R.id.content, DetailCallLogFragment.newInstance(username, displayName, lastCallLogStartTimestamp), false)
    }
}