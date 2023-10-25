package com.dotanphu.sipapp.ui.dialer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.databinding.FragmentDialerBinding
import com.dotanphu.sipapp.ui.call.IncomingCallActivity
import com.dotanphu.sipapp.ui.call.OutgoingCallActivity
import com.dotanphu.sipapp.utils.PermissionsHelper
import com.utils.LogUtil
import com.widget.ToastColor
import dagger.hilt.android.AndroidEntryPoint
import org.linphone.core.Account
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub
import org.linphone.core.Factory
import org.linphone.core.RegistrationState
import javax.inject.Inject

@AndroidEntryPoint
class DialerFragment : BaseFragment() {
    companion object {
        fun newInstance(): DialerFragment {
            val args = Bundle()
            val fragment = DialerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var dataManager: DataManager

//    private lateinit var core: Core
    private lateinit var binding: FragmentDialerBinding

//    private val coreListener = object : CoreListenerStub() {
//        override fun onAccountRegistrationStateChanged(core: Core, account: Account, state: RegistrationState?, message: String) {
//            if (state == RegistrationState.Failed) {
//                LogUtil.wtf("Failed")
//            } else if (state == RegistrationState.Ok) {
//                LogUtil.wtf("Ok")
//            }
//        }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDialerBinding.inflate(inflater, container, false)
        initData()
        listener()
        return binding.root
    }

    private fun initData() {
//        val factory = Factory.instance()
//        core = factory.createCore(null, null, requireContext())

        // We will need the RECORD_AUDIO permission for video call
        val permissionsHelper = PermissionsHelper(this)

        permissionsHelper.requestAllPermissions(onPermissionGranted = {
            // Xử lý khi tất cả quyền đã được cấp
            ToastColor.success(requireContext(), "tất cả quyền đã được cấp")
        }, onPermissionDenied = {
            // Xử lý khi một hoặc nhiều quyền đã bị từ chối
            ToastColor.error(requireContext(), "một hoặc nhiều quyền đã bị từ chối")
        })
    }

    private fun listener() {
        binding.numpad.number0.setOnClickListener { appendToEditText("0") }
        binding.numpad.number1.setOnClickListener { appendToEditText("1") }
        binding.numpad.number2.setOnClickListener { appendToEditText("2") }
        binding.numpad.number3.setOnClickListener { appendToEditText("3") }
        binding.numpad.number4.setOnClickListener { appendToEditText("4") }
        binding.numpad.number5.setOnClickListener { appendToEditText("5") }
        binding.numpad.number6.setOnClickListener { appendToEditText("6") }
        binding.numpad.number7.setOnClickListener { appendToEditText("7") }
        binding.numpad.number8.setOnClickListener { appendToEditText("8") }
        binding.numpad.number9.setOnClickListener { appendToEditText("9") }

        binding.bConnect.setOnClickListener {
            //login()
            requireActivity().startActivity(IncomingCallActivity.newIntent(requireContext()))
        }
        binding.bCall.setOnClickListener {
            val phone = binding.sipUriInput.text.toString()
            requireActivity().startActivity(OutgoingCallActivity.newIntent(requireContext(), phone))
        }
        binding.imgDelete.setOnClickListener {
            val currentText = binding.sipUriInput.text.toString()
            if (currentText.isNotEmpty()) {
                val newText = currentText.substring(0, currentText.length - 1)
                binding.sipUriInput.setText(newText)
                binding.sipUriInput.setSelection(newText.length) // Đặt con trỏ ở cuối văn bản
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun appendToEditText(text: String) {
        val currentText = binding.sipUriInput.text.toString()
        binding.sipUriInput.setText(currentText + text)
    }

//    private fun login() {
//        val username = dataManager.mPreferenceHelper.username.toString()
//        val password = dataManager.mPreferenceHelper.password
//        val domain = dataManager.mPreferenceHelper.domain
//        val transportType = dataManager.mPreferenceHelper.transportType
//        val authInfo = Factory.instance()
//            .createAuthInfo(username, null, password, null, null, domain, null)
//
//        val params = core.createAccountParams()
//        val identity = Factory.instance().createAddress("sip:$username@$domain")
//        params.identityAddress = identity
//
//        val address = Factory.instance().createAddress("sip:$domain")
//        address?.transport = transportType
//        params.serverAddress = address
//        params.isRegisterEnabled = true
//        val account = core.createAccount(params)
//
//        core.addAuthInfo(authInfo)
//        core.addAccount(account)
//
//        core.defaultAccount = account
//        core.addListener(coreListener)
//        core.start()
//    }
}