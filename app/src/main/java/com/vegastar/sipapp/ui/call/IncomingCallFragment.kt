package com.vegastar.sipapp.ui.call

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vegastar.sipapp.component.base.BaseFragment
import com.vegastar.sipapp.data.DataManager
import com.vegastar.sipapp.data.model.event.NotifyEvent
import com.vegastar.sipapp.databinding.FragmentIncomingCallBinding
import com.vegastar.sipapp.utils.NotificationUtil
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_CALL
import com.vegastar.sipapp.utils.core.CallStateChangeListener
import com.vegastar.sipapp.utils.core.CoreHelper
import com.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@AndroidEntryPoint
class IncomingCallFragment : BaseFragment(), CallStateChangeListener {
    companion object {
        fun newInstance(): IncomingCallFragment {
            val args = Bundle()
            val fragment = IncomingCallFragment()
            fragment.arguments = args
            return fragment
        }

        fun newNotificationInstance(checkAcceptCall: Boolean): IncomingCallFragment {
            val args = Bundle()
            args.putBoolean(KEY_CALL, checkAcceptCall)
            val fragment = IncomingCallFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var dataManager: DataManager

    private lateinit var binding: FragmentIncomingCallBinding
    private var checkAcceptCall: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentIncomingCallBinding.inflate(inflater, container, false)
        receiver()
        initData()
        listener()
        checkAcceptCall()
        return binding.root
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NotifyEvent) {
        LogUtil.wtf("IncomingCall")
        Handler(Looper.getMainLooper()).postDelayed({
            requireActivity().finish()
        }, 700)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun receiver() {
        val bundle = arguments
        if (bundle != null) {
            checkAcceptCall = bundle.getBoolean(KEY_CALL)
        }
    }

    private fun initData() {
        //Dừng dịch vụ PrepareCallService
        //PrepareCallService.stop(getApplicationContext())
        //Ẩn thông báo cuộc gọi đến và tắt nhạc chuông nếu có
        NotificationUtil.cancelIncomingNotification(requireContext())

        CoreHelper.getInstance(requireContext())?.start()
        CoreHelper.getInstance(requireContext())?.callStateChangeListener = this

        val remoteAddress = CoreHelper.getInstance(requireContext())?.getRemoteAddress()
        binding.calleeAddress.text = remoteAddress
    }

    private fun listener() {
        binding.bHangupIncomingCall.setOnClickListener {
            CoreHelper.getInstance(requireContext())?.hangUpIncomingCall()
        }
        binding.bHangup.setOnClickListener {
            CoreHelper.getInstance(requireContext())?.hangUpIncomingCall()
        }
        binding.bAnswerIncomingCall.setOnClickListener {
            CoreHelper.getInstance(requireContext())?.answer()
        }
        binding.bSpeaker.setOnClickListener {
            CoreHelper.getInstance(requireContext())?.toggleSpeaker()
        }
        binding.bMicrophone.setOnClickListener {
            CoreHelper.getInstance(requireContext())?.toogleMicrophone()
        }
    }

    private fun checkAcceptCall() {
        if (checkAcceptCall) {
            CoreHelper.getInstance(requireContext())?.answer()
        }
    }

    override fun onCallStateChanged(isConnected: Boolean) {
        if (isConnected) {
            binding.llAcceptCall.visibility = View.VISIBLE
            binding.llIncomingCall.visibility = View.GONE
        } else {
            binding.llAcceptCall.visibility = View.GONE
            binding.llIncomingCall.visibility = View.VISIBLE
        }
    }
}