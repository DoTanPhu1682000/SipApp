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
        fun newInstance(phone: String?): OutgoingCallFragment {
            val args = Bundle()
            args.putString(KEY_PHONE, phone)
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
            }, 5000)
        }
    }

    private fun listener() {
        binding.bHangup.setOnClickListener {
            CoreHelper.getInstance(requireContext())?.hangUpOutgoingCall()
        }
    }

    private fun getData() {
        val tokenFCMDevice1 = "fGCOk4lwRWyHU7ZxrEoAeB:APA91bFTfbpwd-E5_JhNATMmxo3C_pM3oYF_Fstv5wya_eg9r7WpvG0EtC3mebrfNMC5Xb3F5UwMSVnqGcwZ_ARNF92lPFl0mKIqnd0Sq3gpAOLbTeFZ43DA36hHCUQEKSfY4H2RsGL3"
        val tokenFCMDevice2 = "fgktZAsZTLOJBqf69qa1S9:APA91bELaa6KC4NCHmMGKhCrajYesPu-r3e5HszCrXC0sJNC4fnh23tCi8dchzm_Z5m021IvvhiJoW6dQQz284UdaRWlJ13Z3jKf74UGUYm_3T5wR8H4Q7dvb6SnW7EsWxqwiJvhH1Kn"

        viewModel.sendNotificationFcmDirect(tokenFCMDevice1, "my_custom_value", "prepare_call")
    }
}