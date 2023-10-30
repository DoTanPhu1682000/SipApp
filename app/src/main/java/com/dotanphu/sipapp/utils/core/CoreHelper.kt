package com.dotanphu.sipapp.utils.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.dotanphu.sipapp.AppConfig.TAG
import com.dotanphu.sipapp.data.prefs.AppPreferenceHelper
import com.dotanphu.sipapp.ui.call.IncomingCallActivity
import com.dotanphu.sipapp.utils.ActivityLifecycle
import com.dotanphu.sipapp.utils.NotificationUtil
import com.utils.LogUtil
import org.linphone.core.Account
import org.linphone.core.Call
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
    var listener: CoreHelperListener? = null

    private val coreListener = object : CoreListenerStub() {
        override fun onAccountRegistrationStateChanged(core: Core, account: Account, state: RegistrationState?, message: String) {
            Log.e(TAG, "Account [${account.params.identityAddress?.asStringUriOnly()}] registration state changed [$state]")
            // Xử lý sự kiện khi trạng thái đăng ký thay đổi
            if (state == RegistrationState.Failed || state == RegistrationState.Cleared) {
                Log.e(TAG, "Failed")
            } else if (state == RegistrationState.Ok) {
                // Thông báo rằng trạng thái đăng ký thành công
                listener?.onRegistrationStateChanged(true)
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
                }

                Call.State.Error -> {

                }

                else -> {}
            }
        }
    }

    init {
        val factory = Factory.instance()
        factory.setDebugMode(true, "Hello Linphone")
        core = factory.createCore(null, null, context)
    }

    fun start() {
        LogUtil.wtf("core.start")
        core.addListener(coreListener)
        core.start()

        if (isCoreRunning()) return
        LogUtil.wtf("core.isRunning")

        val appPreferenceHelper = AppPreferenceHelper(context)
        val username = appPreferenceHelper.username.toString()
        val password = appPreferenceHelper.password.toString()
        val domain = "192.168.14.209"
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
    }

    fun stop() {
        core.stop()
        core.removeListener(coreListener)
    }

    fun login() {
        core.addListener(coreListener)
        core.start()

        val appPreferenceHelper = AppPreferenceHelper(context)
        val username = appPreferenceHelper.username.toString()
        val password = appPreferenceHelper.password.toString()
        val domain = "192.168.14.209"
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
    }

    fun isCoreRunning(): Boolean {
        val globalState = core.globalState
        return globalState != GlobalState.Shutdown && globalState != GlobalState.Off
    }

    fun outgoingCall(phone: String) {
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
}