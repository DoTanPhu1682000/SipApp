package com.dotanphu.sipapp.ui.login

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dotanphu.sipapp.component.base.BaseViewModel
import org.linphone.core.Account
import org.linphone.core.AccountCreator
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub
import org.linphone.core.RegistrationState
import org.linphone.core.TransportType
import org.linphone.core.tools.Log

class AccountLoginViewModelFactory(private val accountCreator: AccountCreator) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountLoginViewModel(accountCreator) as T
    }
}

class AccountLoginViewModel(private val accountCreator: AccountCreator) : BaseViewModel() {
    val username = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    val domain = MutableLiveData<String>()

    val displayName = MutableLiveData<String>()

    val transport = MutableLiveData<TransportType>()

    val loginEnabled: MediatorLiveData<Boolean> = MediatorLiveData()

    val waitForServerAnswer = MutableLiveData<Boolean>()

    private var accountToCheck: Account? = null
    private var core: Core? = null

    private val coreListener = object : CoreListenerStub() {
        override fun onAccountRegistrationStateChanged(core: Core, account: Account, state: RegistrationState?, message: String) {
            if (account == accountToCheck) {
                Log.i("[Assistant] [Generic Login] Registration state is $state: $message")
                if (state == RegistrationState.Ok) {
                    waitForServerAnswer.value = false
                    core.removeListener(this)
                } else if (state == RegistrationState.Failed) {
                    waitForServerAnswer.value = false
                    core.removeListener(this)
                }
            }
        }
    }

    init {
        transport.value = TransportType.Tls

        loginEnabled.value = false
        loginEnabled.addSource(username) {
            loginEnabled.value = isLoginButtonEnabled()
        }
        loginEnabled.addSource(password) {
            loginEnabled.value = isLoginButtonEnabled()
        }
        loginEnabled.addSource(domain) {
            loginEnabled.value = isLoginButtonEnabled()
        }
    }

    fun setTransport(transportType: TransportType) {
        transport.value = transportType
    }

    fun removeInvalidProxyConfig() {
        val account = accountToCheck
        account ?: return

        val authInfo = account.findAuthInfo()
        if (authInfo != null) core?.removeAuthInfo(authInfo)
        core?.removeAccount(account)
        accountToCheck = null

        // Make sure there is a valid default account
        val accounts = core?.accountList
        if (accounts != null) {
            if (accounts.isNotEmpty() && core?.defaultAccount == null) {
                core?.defaultAccount = accounts.first()
                core?.refreshRegisters()
            }
        }
    }

    fun createAccountAndAuthInfo() {
        waitForServerAnswer.value = true
        core?.addListener(coreListener)

        accountCreator.username = username.value
        accountCreator.password = password.value
        accountCreator.domain = domain.value
        accountCreator.displayName = displayName.value
        accountCreator.transport = transport.value

        val account = accountCreator.createAccountInCore()
        accountToCheck = account

        if (account == null) {
            Log.e("[Assistant] [Generic Login] Account creator couldn't create account")
            core?.removeListener(coreListener)
            waitForServerAnswer.value = false
            return
        }

        Log.i("[Assistant] [Generic Login] Account created")
    }

    private fun isLoginButtonEnabled(): Boolean {
        return username.value.orEmpty().isNotEmpty() && domain.value.orEmpty()
            .isNotEmpty() && password.value.orEmpty().isNotEmpty()
    }
}