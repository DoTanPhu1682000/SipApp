package com.dotanphu.sipapp.ui.call

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.databinding.FragmentIncomingCallBinding
import com.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import org.linphone.core.Account
import org.linphone.core.AudioDevice
import org.linphone.core.Call
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub
import org.linphone.core.Factory
import org.linphone.core.RegistrationState
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
    }

    @Inject
    lateinit var dataManager: DataManager

    private lateinit var core: Core
    private lateinit var binding: FragmentIncomingCallBinding

    private val coreListener = object : CoreListenerStub() {
        override fun onAccountRegistrationStateChanged(core: Core, account: Account, state: RegistrationState?, message: String) {
            if (state == RegistrationState.Failed) {
                LogUtil.wtf("IncomingCall - Failed")
            } else if (state == RegistrationState.Ok) {
                LogUtil.wtf("IncomingCall - Ok")
            }
        }

        override fun onAudioDeviceChanged(core: Core, audioDevice: AudioDevice) {
            // This callback will be triggered when a successful audio device has been changed
        }

        override fun onAudioDevicesListUpdated(core: Core) {
            // This callback will be triggered when the available devices list has changed,
            // for example after a bluetooth headset has been connected/disconnected.
        }

        override fun onCallStateChanged(core: Core, call: Call, state: Call.State?, message: String) {
            // When a call is received
            when (state) {
                Call.State.IncomingReceived -> {
                    binding.calleeAddress.text = call.remoteAddress.asStringUriOnly()
                }

                Call.State.Connected -> {

                }

                Call.State.Released -> {
                    requireActivity().finish()
                }

                else -> {}
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentIncomingCallBinding.inflate(inflater, container, false)
        initData()
        listener()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.wtf("onDestroy - in")
    }

    private fun initData() {
        val factory = Factory.instance()
        factory.setDebugMode(true, "Hello Linphone")
        core = factory.createCore(null, null, requireContext())

        login()
    }

    private fun listener() {
        binding.buttons.bHangup.setOnClickListener {
            // Terminates the call, whether it is ringing or running
            core.currentCall?.terminate()
        }
        binding.buttons.bAnswer.setOnClickListener {
            // if we wanted, we could create a CallParams object
            // and answer using this object to make changes to the call configuration
            // (see OutgoingCall tutorial)
            core.currentCall?.accept()
        }
    }

    private fun login() {
        val username = dataManager.mPreferenceHelper.username.toString()
        val password = dataManager.mPreferenceHelper.password
        val domain = dataManager.mPreferenceHelper.domain
        val transportType = dataManager.mPreferenceHelper.transportType
        val authInfo = Factory.instance()
            .createAuthInfo(username, null, password, null, null, domain, null)

        val params = core.createAccountParams()
        val identity = Factory.instance().createAddress("sip:$username@$domain")
        params.identityAddress = identity

        val address = Factory.instance().createAddress("sip:$domain")
        address?.transport = transportType
        params.serverAddress = address
        params.isRegisterEnabled = true
        val account = core.createAccount(params)

        core.addAuthInfo(authInfo)
        core.addAccount(account)

        // Asks the CaptureTextureView to resize to match the captured video's size ratio
        core.config.setBool("video", "auto_resize_preview_to_keep_ratio", true)

        core.defaultAccount = account
        core.addListener(coreListener)
        core.start()
    }
}