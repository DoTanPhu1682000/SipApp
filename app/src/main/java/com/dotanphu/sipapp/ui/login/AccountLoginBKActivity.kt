package com.dotanphu.sipapp.ui.login

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.databinding.ActivityAccountLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import org.linphone.core.Account
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub
import org.linphone.core.Factory
import org.linphone.core.RegistrationState
import org.linphone.core.TransportType
import org.linphone.core.tools.Log

@AndroidEntryPoint
class AccountLoginBKActivity : AppCompatActivity() {
    private lateinit var core: Core
    private lateinit var binding: ActivityAccountLoginBinding

    // Create a Core listener to listen for the callback we need
    // In this case, we want to know about the account registration status
    private val coreListener = object : CoreListenerStub() {
        override fun onAccountRegistrationStateChanged(core: Core, account: Account, state: RegistrationState?, message: String) {
            // If account has been configured correctly, we will go through Progress and Ok states
            // Otherwise, we will be Failed.
            binding.registrationStatus.text = message

            if (state == RegistrationState.Failed || state == RegistrationState.Cleared) {
                binding.connect.isEnabled = true
            } else if (state == RegistrationState.Ok) {
                binding.disconnect.isEnabled = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initData()
        listener()
    }

    private fun initData() {
        val factory = Factory.instance()
        factory.setDebugMode(true, "Hello Linphone")
        core = factory.createCore(null, null, this)

        val coreVersion = findViewById<TextView>(R.id.core_version)
        coreVersion.text = core.version
    }

    private fun listener() {
        binding.connect.setOnClickListener {
            login()
            it.isEnabled = true
        }
        binding.disconnect.setOnClickListener {
            unregister()
            it.isEnabled = true
        }
        binding.delete.setOnClickListener {
            delete()
            it.isEnabled = true
        }
    }

    private fun login() {
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()
        val domain = binding.domain.text.toString()
        // Get the transport protocol to use.
        // TLS is strongly recommended
        // Only use UDP if you don't have the choice
        val transportType = when (findViewById<RadioGroup>(R.id.transport).checkedRadioButtonId) {
            R.id.udp -> TransportType.Udp
            R.id.tcp -> TransportType.Tcp
            else -> TransportType.Tls
        }

        // To configure a SIP account, we need an Account object and an AuthInfo object
        // The first one is how to connect to the proxy server, the second one stores the credentials

        // The auth info can be created from the Factory as it's only a data class
        // userID is set to null as it's the same as the username in our case
        // ha1 is set to null as we are using the clear text password. Upon first register, the hash will be computed automatically.
        // The realm will be determined automatically from the first register, as well as the algorithm
        val authInfo = Factory.instance()
            .createAuthInfo(username, null, password, null, null, domain, null)

        // Account object replaces deprecated ProxyConfig object
        // Account object is configured through an AccountParams object that we can obtain from the Core
        val accountParams = core.createAccountParams()

        // A SIP account is identified by an identity address that we can construct from the username and domain
        val identity = Factory.instance().createAddress("sip:$username@$domain")
        accountParams.identityAddress = identity

        // We also need to configure where the proxy server is located
        val address = Factory.instance().createAddress("sip:$domain")
        // We use the Address object to easily set the transport protocol
        address?.transport = transportType
        accountParams.serverAddress = address
        // And we ensure the account will start the registration process
        accountParams.isRegisterEnabled = true

        // Now that our AccountParams is configured, we can create the Account object
        val account = core.createAccount(accountParams)

        // Now let's add our objects to the Core
        core.addAuthInfo(authInfo)
        core.addAccount(account)

        // Also set the newly added account as default
        core.defaultAccount = account

        // Allow account to be removed
        binding.delete.isEnabled = true

        // To be notified of the connection status of our account, we need to add the listener to the Core
        core.addListener(coreListener)
        // We can also register a callback on the Account object
        account.addListener { _, state, message ->
            // There is a Log helper in org.linphone.core.tools package
            Log.i("[Account] Registration state changed: $state, $message")
        }

        // Finally we need the Core to be started for the registration to happen (it could have been started before)
        core.start()
    }

    private fun unregister() {
        // Here we will disable the registration of our Account
        val account = core.defaultAccount
        account ?: return

        val params = account.params
        // Returned params object is const, so to make changes we first need to clone it
        val clonedParams = params.clone()

        // Now let's make our changes
        clonedParams.isRegisterEnabled = false

        // And apply them
        account.params = clonedParams
    }

    private fun delete() {
        // To completely remove an Account
        val account = core.defaultAccount
        account ?: return
        core.removeAccount(account)

        // To remove all accounts use
        core.clearAccounts()

        // Same for auth info
        core.clearAllAuthInfo()
    }
}