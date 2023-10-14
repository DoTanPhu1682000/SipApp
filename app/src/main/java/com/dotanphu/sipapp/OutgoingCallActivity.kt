package com.dotanphu.sipapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import com.dotanphu.sipapp.databinding.ActivityOutgoingCallBinding
import org.linphone.core.*

class OutgoingCallActivity : AppCompatActivity() {
    private lateinit var core: Core
    private lateinit var binding: ActivityOutgoingCallBinding

    private val coreListener = object : CoreListenerStub() {
        override fun onAccountRegistrationStateChanged(core: Core, account: Account, state: RegistrationState?, message: String) {
            binding.registrationStatus.text = message

            if (state == RegistrationState.Failed) {
                binding.connect.isEnabled = true
            } else if (state == RegistrationState.Ok) {
                binding.registerLayout.visibility = View.GONE
                binding.callLayout.visibility = View.VISIBLE
            }
        }

        override fun onCallStateChanged(core: Core, call: Call, state: Call.State?, message: String) {
            // This function will be called each time a call state changes,
            // which includes new incoming/outgoing calls
            binding.callStatus.text = message

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
                    binding.pause.isEnabled = true
                    binding.pause.text = "Pause"
                    binding.toggleVideo.isEnabled = true

                    // Only enable toggle camera button if there is more than 1 camera and the video is enabled
                    // We check if core.videoDevicesList.size > 2 because of the fake camera with static image created by our SDK (see below)
                    binding.toggleCamera.isEnabled = core.videoDevicesList.size > 2 && call.currentParams.isVideoEnabled
                }

                Call.State.Paused -> {
                    // When you put a call in pause, it will became Paused
                    binding.pause.text = "Resume"
                    binding.toggleVideo.isEnabled = false
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
                    binding.remoteAddress.isEnabled = true
                    binding.call.isEnabled = true
                    binding.pause.isEnabled = false
                    binding.pause.text = "Pause"
                    binding.toggleVideo.isEnabled = false
                    binding.hangUp.isEnabled = false
                    binding.toggleCamera.isEnabled = false
                }

                Call.State.Error -> {

                }

                else -> {}
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutgoingCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
    }

    private fun initData() {
        val factory = Factory.instance()
        factory.setDebugMode(true, "Hello Linphone")
        core = factory.createCore(null, null, this)

        binding.connect.setOnClickListener {
            login()
            it.isEnabled = false
        }

        // For video to work, we need two TextureViews:
        // one for the remote video and one for the local preview
        core.nativeVideoWindowId = findViewById(R.id.remote_video_surface)
        // The local preview is a org.linphone.mediastream.video.capture.CaptureTextureView
        // which inherits from TextureView and contains code to keep the ratio of the capture video
        core.nativePreviewWindowId = findViewById(R.id.local_preview_video_surface)

        // Here we enable the video capture & display at Core level
        // It doesn't mean calls will be made with video automatically,
        // But it allows to use it later
        core.isVideoCaptureEnabled = true
        core.isVideoDisplayEnabled = true

        // When enabling the video, the remote will either automatically answer the update request
        // or it will ask it's user depending on it's policy.
        // Here we have configured the policy to always automatically accept video requests
        core.videoActivationPolicy.automaticallyAccept = true
        // If you don't want to automatically accept,
        // you'll have to use a code similar to the one in toggleVideo to answer a received request

        // If the following property is enabled, it will automatically configure created call params with video enabled
        //core.videoActivationPolicy.automaticallyInitiate = true

        binding.call.setOnClickListener {
            outgoingCall()
            binding.remoteAddress.isEnabled = false
            it.isEnabled = false
            binding.hangUp.isEnabled = true
        }

        binding.hangUp.setOnClickListener {
            hangUp()
        }

        binding.pause.setOnClickListener {
            pauseOrResume()
        }

        binding.toggleVideo.setOnClickListener {
            toggleVideo()
        }

        binding.toggleCamera.setOnClickListener {
            toggleCamera()
        }

        binding.pause.isEnabled = false
        binding.toggleVideo.isEnabled = false
        binding.toggleCamera.isEnabled = false
        binding.hangUp.isEnabled = false
    }

    private fun login() {
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()
        val domain = binding.domain.text.toString()
        val transportType = when (findViewById<RadioGroup>(R.id.transport).checkedRadioButtonId) {
            R.id.udp -> TransportType.Udp
            R.id.tcp -> TransportType.Tcp
            else -> TransportType.Tls
        }
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
        if (packageManager.checkPermission(Manifest.permission.RECORD_AUDIO, packageName) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.RECORD_AUDIO), 0)
            return
        }
    }

    private fun outgoingCall() {
        // As for everything we need to get the SIP URI of the remote and convert it to an Address
        val remoteSipUri = binding.remoteAddress.text.toString()
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

    private fun toggleVideo() {
//        if (core.callsNb == 0) return
//        val call = if (core.currentCall != null) core.currentCall else core.calls[0]
//        call ?: return
//
//        // We will need the CAMERA permission for video call
//        if (packageManager.checkPermission(android.Manifest.permission.CAMERA, packageName) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 0)
//            return
//        }
//
//        // To update the call, we need to create a new call params, from the call object this time
//        val params = core.createCallParams(call)
//        // Here we toggle the video state (disable it if enabled, enable it if disabled)
//        // Note that we are using currentParams and not params or remoteParams
//        // params is the object you configured when the call was started
//        // remote params is the same but for the remote
//        // current params is the real params of the call, resulting of the mix of local & remote params
//        params?.enableVideo(!call.currentParams.videoEnabled())
//        // Finally we request the call update
//        call.update(params)
//
//        // Note that when toggling off the video, TextureViews will keep showing the latest frame displayed
    }

    private fun toggleCamera() {
        // Currently used camera
        val currentDevice = core.videoDevice

        // Let's iterate over all camera available and choose another one
        for (camera in core.videoDevicesList) {
            // All devices will have a "Static picture" fake camera, and we don't want to use it
            if (camera != currentDevice && camera != "StaticImage: Static picture") {
                core.videoDevice = camera
                break
            }
        }
    }

    private fun pauseOrResume() {
        if (core.callsNb == 0) return
        val call = if (core.currentCall != null) core.currentCall else core.calls[0]
        call ?: return

        if (call.state != Call.State.Paused && call.state != Call.State.Pausing) {
            // If our call isn't paused, let's pause it
            call.pause()
        } else if (call.state != Call.State.Resuming) {
            // Otherwise let's resume it
            call.resume()
        }
    }
}