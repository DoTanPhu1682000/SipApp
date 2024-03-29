package com.vegastar.sipapp.utils.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.utils.LogUtil
import com.vegastar.sipapp.AppConfig.ADDRESS_SIP
import com.vegastar.sipapp.AppConfig.TAG
import com.vegastar.sipapp.component.service.PrepareCallService
import com.vegastar.sipapp.data.model.event.NotifyEvent
import com.vegastar.sipapp.data.prefs.AppPreferenceHelper
import com.vegastar.sipapp.ui.call.IncomingCallActivity
import com.vegastar.sipapp.utils.ActivityLifecycle
import com.vegastar.sipapp.utils.NotificationUtil
import org.greenrobot.eventbus.EventBus
import org.linphone.core.Account
import org.linphone.core.AudioDevice
import org.linphone.core.Call
import org.linphone.core.CallLog
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub
import org.linphone.core.Factory
import org.linphone.core.GlobalState
import org.linphone.core.MediaEncryption
import org.linphone.core.RegistrationState
import org.linphone.core.TransportType

interface CoreHelperListener {
    fun onRegistrationStateChanged(isSuccessful: Boolean)
}

class CoreHelper(val context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: CoreHelper? = null
        fun getInstance(context: Context): CoreHelper? {
            if (instance == null) {
                instance = CoreHelper(context)
            }
            return instance
        }
    }

    val core: Core
    private var accountToCheck: Account? = null

    var listener: CoreHelperListener? = null
    var callStateChangeListener: CallStateChangeListener? = null
    private var isMicrophoneMuted = false

    private val coreListener = object : CoreListenerStub() {
        override fun onAccountRegistrationStateChanged(core: Core, account: Account, state: RegistrationState?, message: String) {
            if (account == accountToCheck) {
                Log.e(TAG, "Account [${account.params.identityAddress?.asStringUriOnly()}] registration state changed [$state]")
                if (state == RegistrationState.Failed || state == RegistrationState.Cleared) {
                    LogUtil.wtf("Cleared")
                    deleteAccount(account)
                    listener?.onRegistrationStateChanged(false)
                } else if (state == RegistrationState.Ok) {
                    listener?.onRegistrationStateChanged(true)
                }
            }
        }

        override fun onAudioDeviceChanged(core: Core, audioDevice: AudioDevice) {
            // This callback will be triggered when a successful audio device has been changed
            LogUtil.wtf("[Call Controls] Audio device changed: ${audioDevice.deviceName}")
        }

        override fun onAudioDevicesListUpdated(core: Core) {
            // This callback will be triggered when the available devices list has changed,
            // for example after a bluetooth headset has been connected/disconnected.
            LogUtil.wtf("[Call Controls] Audio devices list updated")
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
                    callStateChangeListener?.onCallStateChanged(true)
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

                Call.State.IncomingReceived -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ActivityLifecycle.instance!!.isBackground) {
                        LogUtil.wtf("start IncomingCallNotification")
                        NotificationUtil.createIncomingCallNotification(context)
                    } else {
                        LogUtil.wtf("start IncomingCallActivity")
                        val intent: Intent = IncomingCallActivity.newIntent(context)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                    }
                }

                Call.State.Released -> {
                    // Call state will be released shortly after the End state
                    LogUtil.wtf("Released")
                    EventBus.getDefault().post(NotifyEvent(NotifyEvent.Type.DEFAULT))

                    //Dừng dịch vụ và xóa thông báo
                    PrepareCallService.stop(context)
                    NotificationUtil.cancelPrepareNotification(context)
                    NotificationUtil.cancelIncomingNotification(context)
                }

                Call.State.Error -> {

                }

                else -> {}
            }
        }

        override fun onCallLogUpdated(core: Core, log: CallLog) {
            LogUtil.wtf("[History Detail] New call log for ${log.remoteAddress.asStringUriOnly()} with local address ${log.localAddress.asStringUriOnly()}")
        }
    }

    init {
        val factory = Factory.instance()
        factory.setDebugMode(true, "Hello Linphone")
        core = factory.createCore(null, null, context)
        core.addListener(coreListener)
    }

    fun start() {
        if (isCoreRunning()) return
        LogUtil.wtf("core.start")

        val appPreferenceHelper = AppPreferenceHelper(context)
        val username = appPreferenceHelper.username.toString()
        val password = appPreferenceHelper.password.toString()
        val domain = ADDRESS_SIP
        val authInfo = Factory.instance()
            .createAuthInfo(username, null, password, null, null, domain, null)

        val accountParams = core.createAccountParams()
        val identity = Factory.instance().createAddress("sip:$username@$domain")
        accountParams.identityAddress = identity

        val address = Factory.instance().createAddress("sip:$domain")
        address?.transport = TransportType.Udp
        accountParams.serverAddress = address
        accountParams.isRegisterEnabled = true

        val account = core.createAccount(accountParams)
        core.addAuthInfo(authInfo)
        core.addAccount(account)
        core.defaultAccount = account

        accountToCheck = account

        core.start()
    }

    fun stop() {
        core.stop()
        core.removeListener(coreListener)
    }

    fun isCoreRunning(): Boolean {
        return core.globalState == GlobalState.On
    }

    fun login() {
        val appPreferenceHelper = AppPreferenceHelper(context)
        val username = appPreferenceHelper.username.toString()
        val password = appPreferenceHelper.password.toString()
        val domain = ADDRESS_SIP
        val authInfo = Factory.instance()
            .createAuthInfo(username, null, password, null, null, domain, null)

        val accountParams = core.createAccountParams()
        val identity = Factory.instance().createAddress("sip:$username@$domain")
        accountParams.identityAddress = identity

        val address = Factory.instance().createAddress("sip:$domain")
        address?.transport = TransportType.Udp
        accountParams.serverAddress = address
        accountParams.isRegisterEnabled = true

        val account = core.createAccount(accountParams)
        core.addAuthInfo(authInfo)
        core.addAccount(account)
        core.defaultAccount = account

        accountToCheck = account

        core.start()
    }

    fun outgoingCall(phone: String) {
        // As for everything we need to get the SIP URI of the remote and convert it to an Address
        val remoteSipUri = "sip:$phone@${ADDRESS_SIP}"
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

    fun hangUpOutgoingCall() {
        if (core.callsNb == 0) return

        // If the call state isn't paused, we can get it using core.currentCall
        val call = if (core.currentCall != null) core.currentCall else core.calls[0]
        call ?: return

        // Terminating a call is quite simple
        call.terminate()
    }

    fun answer() {
        // if we wanted, we could create a CallParams object
        // and answer using this object to make changes to the call configuration
        // (see OutgoingCall tutorial)
        core.currentCall?.accept()
    }

    fun hangUpIncomingCall() {
        // Terminates the call, whether it is ringing or running
        core.currentCall?.terminate()
    }

    fun toggleSpeaker() {
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

    fun toogleMicrophone() {
        core.currentCall?.let { currentCall ->
            isMicrophoneMuted = !isMicrophoneMuted
            currentCall.microphoneMuted = isMicrophoneMuted
        }
    }

    fun isCallLogMissed(callLog: CallLog): Boolean {
        return (callLog.dir == Call.Dir.Incoming && (callLog.status == Call.Status.Missed || callLog.status == Call.Status.Aborted || callLog.status == Call.Status.EarlyAborted))
    }

    fun startDeleteAccount() {
        val account = core.defaultAccount
        Log.e("[Account Settings]", "Starting to delete account [${account?.params?.identityAddress?.asStringUriOnly()}]")

        val registered = account?.state == RegistrationState.Ok

        val params = account?.params?.clone()
        params?.isRegisterEnabled = false
        if (params != null) {
            account.params = params
        }

        if (!registered) {
            Log.e("[Account Settings]", "Account isn't registered, don't unregister before removing it")
            if (account != null) {
                deleteAccount(account)
            }
        } else {
            Log.e("[Account Settings]", "Waiting for account registration to be cleared before removing it")
        }
    }

    private fun deleteAccount(account: Account) {
        val authInfo = account.findAuthInfo()
        if (authInfo != null) {
            Log.e("[Account Settings]", "Found auth info $authInfo, removing it.")
            core.removeAuthInfo(authInfo)
        } else {
            Log.e("[Account Settings]", "Couldn't find matching auth info...")
        }

        core.removeAccount(account)
    }
}