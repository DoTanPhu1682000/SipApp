package com.dotanphu.sipapp.ui.call

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.databinding.FragmentOutgoingCallBinding
import com.dotanphu.sipapp.utils.constant.KeyConstant.KEY_PHONE
import com.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import org.linphone.core.Account
import org.linphone.core.Call
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub
import org.linphone.core.Factory
import org.linphone.core.MediaEncryption
import org.linphone.core.RegistrationState
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

    private val RECORD_AUDIO_PERMISSION_CODE = 1
    private lateinit var core: Core
    private lateinit var binding: FragmentOutgoingCallBinding

    private var phone: String = ""

    private val coreListener = object : CoreListenerStub() {
        override fun onAccountRegistrationStateChanged(core: Core, account: Account, state: RegistrationState?, message: String) {
            if (state == RegistrationState.Failed) {
                LogUtil.wtf("OutgoingCall - Failed")
            } else if (state == RegistrationState.Ok) {
                LogUtil.wtf("OutgoingCall - Ok")
            }
        }

        override fun onCallStateChanged(core: Core, call: Call, state: Call.State?, message: String) {
            // This function will be called each time a call state changes,
            // which includes new incoming/outgoing calls
            when (state) {
                Call.State.OutgoingInit -> {
                    // First state an outgoing call will go through
                }

                Call.State.OutgoingProgress -> {
                    // Right after outgoing init
                }

                Call.State.OutgoingRinging -> {
                    // This state will be reached upon reception of the 180 RINGING
                }

                Call.State.Connected -> {
                    // When the 200 OK has been received
                }

                Call.State.StreamsRunning -> {
                    // This state indicates the call is active.
                    // You may reach this state multiple times, for example after a pause/resume
                    // or after the ICE negotiation completes
                    // Wait for the call to be connected before allowing a call update
                }

                Call.State.Paused -> {
                    // When you put a call in pause, it will became Paused
                }

                Call.State.PausedByRemote -> {
                    // When the remote end of the call pauses it, it will be PausedByRemote
                }

                Call.State.Updating -> {
                    // When we request a call update, for example when toggling video
                }

                Call.State.UpdatedByRemote -> {
                    // When the remote requests a call update
                }

                Call.State.Released -> {
                    // Call state will be released shortly after the End state
                }

                Call.State.Error -> {

                }

                else -> {}
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOutgoingCallBinding.inflate(inflater, container, false)
        receiver()
        initData()
        listener()
        return binding.root
    }

    private fun receiver() {
        val bundle = arguments
        if (bundle != null) {
            phone = bundle.getString(KEY_PHONE).toString()
        }
    }

    private fun initData() {
        val factory = Factory.instance()
        factory.setDebugMode(true, "Hello Linphone")
        core = factory.createCore(null, null, requireContext())

        login()
    }

    private fun listener() {
        binding.buttons.bHangup.setOnClickListener {
            hangUp()
        }
        binding.buttons.bMicrophone.setOnClickListener {
            toggleMuteMicrophone()
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

        // We will need the RECORD_AUDIO permission for video call
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), RECORD_AUDIO_PERMISSION_CODE)
        }

        outgoingCall()
    }

    private fun outgoingCall() {
        // As for everything we need to get the SIP URI of the remote and convert it to an Address
        val remoteSipUri = "sip:$phone@192.168.14.209"
        val remoteAddress = Factory.instance().createAddress(remoteSipUri)
        remoteAddress ?: return // If address parsing fails, we can't continue with outgoing call process

        // We also need a CallParams object
        // Create call params expects a Call object for incoming calls, but for outgoing we must use null safely
        val params = core.createCallParams(null)
        params ?: return // Same for params

        // We can now configure it
        // Here we ask for no encryption but we could ask for ZRTP/SRTP/DTLS
        params.mediaEncryption = MediaEncryption.None
        // If we wanted to start the call with video directly
        //params.enableVideo(true)

        // Finally we start the call
        core.inviteAddressWithParams(remoteAddress, params)
        // Call process can be followed in onCallStateChanged callback from core listener
    }

    private fun hangUp() {
        if (core.callsNb == 0) return

        // If the call state isn't paused, we can get it using core.currentCall
        val call = if (core.currentCall != null) core.currentCall else core.calls[0]
        call ?: return

        // Terminating a call is quite simple
        call.terminate()
    }

    private fun toggleMuteMicrophone() {
        
    }
}