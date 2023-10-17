package com.dotanphu.sipapp.ui.call

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.databinding.ActivityIncomingCallBinding
import org.linphone.core.Account
import org.linphone.core.AudioDevice
import org.linphone.core.Call
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub
import org.linphone.core.Factory
import org.linphone.core.RegistrationState
import org.linphone.core.TransportType

class IncomingCallActivity : AppCompatActivity() {
    private lateinit var core: Core
    private lateinit var binding: ActivityIncomingCallBinding

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

        override fun onAudioDeviceChanged(core: Core, audioDevice: AudioDevice) {
            // This callback will be triggered when a successful audio device has been changed
        }

        override fun onAudioDevicesListUpdated(core: Core) {
            // This callback will be triggered when the available devices list has changed,
            // for example after a bluetooth headset has been connected/disconnected.
        }

        override fun onCallStateChanged(core: Core, call: Call, state: Call.State?, message: String) {
            binding.callStatus.text = message

            // When a call is received
            when (state) {
                Call.State.IncomingReceived -> {
                    binding.hangUp.isEnabled = true
                    binding.answer.isEnabled = true
                    binding.remoteAddress.setText(call.remoteAddress.asStringUriOnly())
                }

                Call.State.Connected -> {
                    binding.muteMic.isEnabled = true
                    binding.toggleSpeaker.isEnabled = true
                }

                Call.State.Released -> {
                    binding.hangUp.isEnabled = false
                    binding.answer.isEnabled = false
                    binding.muteMic.isEnabled = false
                    binding.toggleSpeaker.isEnabled = false
                    binding.remoteAddress.text.clear()
                }

                else -> {}
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomingCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        listener()
    }

    private fun initData() {
        val factory = Factory.instance()
        factory.setDebugMode(true, "Hello Linphone")
        core = factory.createCore(null, null, this)

        binding.hangUp.isEnabled = false
        binding.answer.isEnabled = false
        binding.muteMic.isEnabled = false
        binding.toggleSpeaker.isEnabled = false
        binding.remoteAddress.isEnabled = false
    }

    private fun listener() {
        binding.connect.setOnClickListener {
            login()
            it.isEnabled = true
        }
        binding.hangUp.setOnClickListener {
            // Terminates the call, whether it is ringing or running
            core.currentCall?.terminate()
        }
        binding.answer.setOnClickListener {
            // if we wanted, we could create a CallParams object
            // and answer using this object to make changes to the call configuration
            // (see OutgoingCall tutorial)
            core.currentCall?.accept()
        }
//        findViewById<Button>(R.id.mute_mic).setOnClickListener {
//            // The following toggles the microphone, disabling completely / enabling the sound capture
//            // from the device microphone
//            core.enableMic(!core.micEnabled())
//        }

        binding.toggleSpeaker.setOnClickListener {
            toggleSpeaker()
        }
    }

    private fun toggleSpeaker() {
        // Get the currently used audio device
        val currentAudioDevice = core.currentCall?.outputAudioDevice
        val speakerEnabled = currentAudioDevice?.type == AudioDevice.Type.Speaker

        // We can get a list of all available audio devices using
        // Note that on tablets for example, there may be no Earpiece device
        for (audioDevice in core.audioDevices) {
            if (speakerEnabled && audioDevice.type == AudioDevice.Type.Earpiece) {
                core.currentCall?.outputAudioDevice = audioDevice
                return
            } else if (!speakerEnabled && audioDevice.type == AudioDevice.Type.Speaker) {
                core.currentCall?.outputAudioDevice = audioDevice
                return
            }/* If we wanted to route the audio to a bluetooth headset
            else if (audioDevice.type == AudioDevice.Type.Bluetooth) {
                core.currentCall?.outputAudioDevice = audioDevice
            }*/
        }
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

        core.defaultAccount = account
        core.addListener(coreListener)
        core.start()

        // We will need the RECORD_AUDIO permission for video call
        if (packageManager.checkPermission(android.Manifest.permission.RECORD_AUDIO, packageName) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.RECORD_AUDIO), 0)
            return
        }
    }
}