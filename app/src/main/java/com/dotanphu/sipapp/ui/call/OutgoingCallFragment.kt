package com.dotanphu.sipapp.ui.call

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.data.model.event.NotifyEvent
import com.dotanphu.sipapp.databinding.FragmentOutgoingCallBinding
import com.dotanphu.sipapp.utils.constant.KeyConstant.KEY_FCM_TOKEN
import com.dotanphu.sipapp.utils.constant.KeyConstant.KEY_PHONE
import com.dotanphu.sipapp.utils.core.CoreHelper
import com.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@AndroidEntryPoint
class OutgoingCallFragment : BaseFragment() {
    companion object {
        fun newInstance(phone: String?, fcmToken: String?): OutgoingCallFragment {
            val args = Bundle()
            args.putString(KEY_PHONE, phone)
            args.putString(KEY_FCM_TOKEN, fcmToken)
            val fragment = OutgoingCallFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var dataManager: DataManager

    private lateinit var binding: FragmentOutgoingCallBinding
    private lateinit var viewModel: OutgoingCallViewModel

    private var phone: String = ""
    private var fcmToken: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOutgoingCallBinding.inflate(inflater, container, false)
        receiver()
        initData()
        observe()
        listener()
        getData()
        return binding.root
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NotifyEvent) {
        LogUtil.wtf("OutgoingCall")
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
            phone = bundle.getString(KEY_PHONE).toString()
            fcmToken = bundle.getString(KEY_FCM_TOKEN).toString()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        viewModel = ViewModelProvider(this)[OutgoingCallViewModel::class.java]

        binding.calleeName.text = phone
        binding.calleeAddress.text = "sip:$phone@192.168.14.209"
    }

    private fun observe() {
        registerObserverBaseEvent(viewModel, viewLifecycleOwner)
        viewModel.onSendNotificationSuccess.observe(viewLifecycleOwner) {
            Handler(Looper.getMainLooper()).postDelayed({
                CoreHelper.getInstance(requireContext())?.start()
                CoreHelper.getInstance(requireContext())?.outgoingCall(phone)
            }, 8000)
        }
    }

    private fun listener() {
        binding.bHangup.setOnClickListener {
            CoreHelper.getInstance(requireContext())?.hangUpOutgoingCall()
        }
        binding.bSpeaker.setOnClickListener {
            CoreHelper.getInstance(requireContext())?.toggleSpeaker()
        }
        binding.bMicrophone.setOnClickListener {
            CoreHelper.getInstance(requireContext())?.toogleMicrophone()
        }
    }

    private fun getData() {
        viewModel.sendNotificationFcmDirect(fcmToken, "my_custom_value", "prepare_call")
    }
}