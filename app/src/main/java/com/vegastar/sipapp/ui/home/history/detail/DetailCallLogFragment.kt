package com.vegastar.sipapp.ui.home.history.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utils.LogUtil
import com.vegastar.sipapp.component.base.BaseFragment
import com.vegastar.sipapp.databinding.FragmentDetailCallLogBinding
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_DISPLAY_NAME
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_LAST_CALL_LOG_START_TIME
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_USER_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCallLogFragment : BaseFragment() {

    companion object {
        fun newInstance(username: String?, displayName: String?, lastCallLogStartTimestamp: String?): DetailCallLogFragment {
            val args = Bundle()
            args.putString(KEY_USER_NAME, username)
            args.putString(KEY_DISPLAY_NAME, displayName)
            args.putString(KEY_LAST_CALL_LOG_START_TIME, lastCallLogStartTimestamp)
            val fragment = DetailCallLogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentDetailCallLogBinding

    private var username: String? = null
    private var displayName: String? = null
    private var lastCallLogStartTimestamp: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailCallLogBinding.inflate(inflater, container, false)
        receiver()
        initData()
        return binding.root
    }

    private fun receiver() {
        val bundle = arguments
        if (bundle != null) {
            username = bundle.getString(KEY_USER_NAME)
            displayName = bundle.getString(KEY_DISPLAY_NAME)
            lastCallLogStartTimestamp = bundle.getString(KEY_LAST_CALL_LOG_START_TIME)
        }
    }

    private fun initData() {
        LogUtil.wtf("$username - $displayName - $lastCallLogStartTimestamp")

        if (displayName == null) {
            binding.tvUserName.text = username
        } else {
            binding.tvUserName.text = displayName
        }
    }
}