package com.dotanphu.sipapp.ui.call

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.data.model.event.NotifyEvent
import com.dotanphu.sipapp.databinding.FragmentIncomingCallBinding
import com.dotanphu.sipapp.utils.NotificationUtil
import com.dotanphu.sipapp.utils.constant.KeyConstant.KEY_CALL
import com.dotanphu.sipapp.utils.core.CoreHelper
import com.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@AndroidEntryPoint
class IncomingCallFragment : BaseFragment() {
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
    }

    private fun listener() {
        binding.buttons.bHangup.setOnClickListener {
            CoreHelper.getInstance(requireContext())?.hangUpIncomingCall()
        }
        binding.buttons.bAnswer.setOnClickListener {
            CoreHelper.getInstance(requireContext())?.answer()
        }
    }

    private fun checkAcceptCall() {
        if (checkAcceptCall) {
            CoreHelper.getInstance(requireContext())?.answer()
        }
    }
}