package com.dotanphu.sipapp.utils.core

import android.content.Context
import android.util.Log
import com.dotanphu.sipapp.AppConfig.TAG
import org.linphone.core.Account
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub
import org.linphone.core.Factory
import org.linphone.core.RegistrationState
import org.linphone.core.TransportType

interface CoreHelperListener {
    fun onRegistrationStateChanged(isSuccessful: Boolean)
}

class CoreHelper(val context: Context) {

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
    }

    init {
        val factory = Factory.instance()
        factory.setDebugMode(true, "Hello Linphone")
        core = factory.createCore(null, null, context)
    }

    fun start() {
        core.addListener(coreListener)
        core.start()
    }

    fun stop() {
        core.stop()
        core.removeListener(coreListener)
    }

    fun login(username: String, password: String) {
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
}