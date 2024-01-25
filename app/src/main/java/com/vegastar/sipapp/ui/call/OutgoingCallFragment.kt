package com.vegastar.sipapp.ui.call

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.lifecycle.ViewModelProvider
import com.utils.LogUtil
import com.vegastar.sipapp.R
import com.vegastar.sipapp.component.base.BaseFragment
import com.vegastar.sipapp.data.DataManager
import com.vegastar.sipapp.data.model.event.NotifyEvent
import com.vegastar.sipapp.databinding.FragmentOutgoingCallBinding
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_DISPLAY_NAME
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_FCM_TOKEN
import com.vegastar.sipapp.utils.constant.KeyConstant.KEY_PHONE
import com.vegastar.sipapp.utils.core.CallStateChangeListener
import com.vegastar.sipapp.utils.core.CoreHelper
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@AndroidEntryPoint
class OutgoingCallFragment : BaseFragment(), CallStateChangeListener {
    companion object {
        fun newInstance(phone: String?, displayName: String?, fcmToken: String?): OutgoingCallFragment {
            val args = Bundle()
            args.putString(KEY_PHONE, phone)
            args.putString(KEY_DISPLAY_NAME, displayName)
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
    private var displayName: String? = null
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
            displayName = bundle.getString(KEY_DISPLAY_NAME)
            fcmToken = bundle.getString(KEY_FCM_TOKEN).toString()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        viewModel = ViewModelProvider(this)[OutgoingCallViewModel::class.java]

        CoreHelper.getInstance(requireContext())?.callStateChangeListener = this

        if (displayName != null) {
            binding.calleeName.text = displayName
        } else {
            binding.calleeName.text = phone
        }

        binding.tvPhone.text = "(${phone})"
    }

    private fun observe() {
        registerObserverBaseEvent(viewModel, viewLifecycleOwner)
        viewModel.onSendNotificationSuccess.observe(viewLifecycleOwner) {
            Handler(Looper.getMainLooper()).postDelayed({
                CoreHelper.getInstance(requireContext())?.start()
                CoreHelper.getInstance(requireContext())?.outgoingCall(phone)
            }, 6000)
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

    private fun startTimer() {
        val timer = binding.root.findViewById<Chronometer>(R.id.active_call_timer)
        timer.format = "%s"

        // Bắt đầu đếm thời gian từ 00:00
        timer.base = SystemClock.elapsedRealtime()
        timer.start()
    }

    override fun onCallStateChanged(isConnected: Boolean) {
        if (isConnected) {
            binding.activeCallTimer.visibility = View.VISIBLE
            binding.calleeRinging.visibility = View.GONE
            startTimer()
        }
    }
}