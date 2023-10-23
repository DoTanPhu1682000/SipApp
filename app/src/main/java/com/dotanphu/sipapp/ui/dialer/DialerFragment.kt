package com.dotanphu.sipapp.ui.dialer

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.databinding.FragmentDialerBinding
import com.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import org.linphone.core.Account
import org.linphone.core.Call
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub
import org.linphone.core.Factory
import org.linphone.core.MediaEncryption
import org.linphone.core.RegistrationState
import org.linphone.core.TransportType
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

    private val RECORD_AUDIO_PERMISSION_CODE = 1
    private lateinit var core: Core
    private lateinit var binding: FragmentDialerBinding

    private val coreListener = object : CoreListenerStub() {
        override fun onAccountRegistrationStateChanged(core: Core, account: Account, state: RegistrationState?, message: String) {
//            binding.registrationStatus.text = message

            if (state == RegistrationState.Failed) {
                LogUtil.wtf("Failed")
//                binding.connect.isEnabled = true
            } else if (state == RegistrationState.Ok) {
                LogUtil.wtf("Ok")
//                binding.registerLayout.visibility = View.GONE
//                binding.callLayout.visibility = View.VISIBLE
            }
        }

        override fun onCallStateChanged(core: Core, call: Call, state: Call.State?, message: String) {
            // This function will be called each time a call state changes,
            // which includes new incoming/outgoing calls
//            binding.callStatus.text = message

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
//                    binding.pause.isEnabled = true
//                    binding.pause.text = "Pause"
//                    binding.toggleVideo.isEnabled = true

                    // Only enable toggle camera button if there is more than 1 camera and the video is enabled
                    // We check if core.videoDevicesList.size > 2 because of the fake camera with static image created by our SDK (see below)
//                    binding.toggleCamera.isEnabled = core.videoDevicesList.size > 2 && call.currentParams.isVideoEnabled
                }

                Call.State.Paused -> {
                    // When you put a call in pause, it will became Paused
//                    binding.pause.text = "Resume"
//                    binding.toggleVideo.isEnabled = false
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
//                    binding.remoteAddress.isEnabled = true
//                    binding.call.isEnabled = true
//                    binding.pause.isEnabled = false
//                    binding.pause.text = "Pause"
//                    binding.toggleVideo.isEnabled = false
//                    binding.hangUp.isEnabled = false
//                    binding.toggleCamera.isEnabled = false
                }

                Call.State.Error -> {

                }

                else -> {}
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDialerBinding.inflate(inflater, container, false)
        initData()
        listener()
        return binding.root
    }

    private fun initData() {
        val factory = Factory.instance()
        core = factory.createCore(null, null, requireContext())

        // For video to work, we need two TextureViews:
        // one for the remote video and one for the local preview
//        core.nativeVideoWindowId = findViewById(R.id.remote_video_surface)
        // The local preview is a org.linphone.mediastream.video.capture.CaptureTextureView
        // which inherits from TextureView and contains code to keep the ratio of the capture video
//        core.nativePreviewWindowId = findViewById(R.id.local_preview_video_surface)

        // Here we enable the video capture & display at Core level
        // It doesn't mean calls will be made with video automatically,
        // But it allows to use it later
//        core.isVideoCaptureEnabled = true
//        core.isVideoDisplayEnabled = true

        // When enabling the video, the remote will either automatically answer the update request
        // or it will ask it's user depending on it's policy.
        // Here we have configured the policy to always automatically accept video requests
//        core.videoActivationPolicy.automaticallyAccept = true
        // If you don't want to automatically accept,
        // you'll have to use a code similar to the one in toggleVideo to answer a received request

        // If the following property is enabled, it will automatically configure created call params with video enabled
        //core.videoActivationPolicy.automaticallyInitiate = true

//        binding.pause.isEnabled = false
//        binding.toggleVideo.isEnabled = false
//        binding.toggleCamera.isEnabled = false
//        binding.hangUp.isEnabled = false
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
            login()
        }
        binding.bCall.setOnClickListener {
            outgoingCall()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun appendToEditText(text: String) {
        val currentText = binding.sipUriInput.text.toString()
        binding.sipUriInput.setText(currentText + text)
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
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RECORD_AUDIO_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Quyền RECORD_AUDIO đã được cấp
                    LogUtil.wtf("Quyền RECORD_AUDIO đã được cấp")
                } else {
                    // Quyền RECORD_AUDIO đã bị từ chối, bạn có thể xử lý tại đây
                    LogUtil.wtf("Quyền RECORD_AUDIO đã bị từ chối")
                }
            }
        }
    }

    private fun outgoingCall() {
        // As for everything we need to get the SIP URI of the remote and convert it to an Address
        //        val remoteSipUri = binding.remoteAddress.text.toString()
        val remoteSipUri = "sip:101@192.168.14.209"
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
}