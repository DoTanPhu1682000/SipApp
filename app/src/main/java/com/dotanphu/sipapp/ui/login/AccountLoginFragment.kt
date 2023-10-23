package com.dotanphu.sipapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dotanphu.sipapp.R
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.databinding.FragmentAccountLoginBinding
import com.dotanphu.sipapp.ui.dialer.DialerActivity
import com.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import org.linphone.core.Account
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub
import org.linphone.core.Factory
import org.linphone.core.RegistrationState
import org.linphone.core.TransportType
import javax.inject.Inject

@AndroidEntryPoint
class AccountLoginFragment : BaseFragment() {
    companion object {
        fun newInstance(): AccountLoginFragment {
            val args = Bundle()
            val fragment = AccountLoginFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var dataManager: DataManager

    private lateinit var core: Core
    private lateinit var binding: FragmentAccountLoginBinding
    private lateinit var viewModel: AccountLoginViewModel

    private val coreListener = object : CoreListenerStub() {
        override fun onAccountRegistrationStateChanged(core: Core, account: Account, state: RegistrationState?, message: String) {
            binding.registrationStatus.text = message

            if (state == RegistrationState.Failed || state == RegistrationState.Cleared) {
                binding.connect.isEnabled = true
            } else if (state == RegistrationState.Ok) {
                binding.disconnect.isEnabled = true
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountLoginBinding.inflate(inflater, container, false)
        initData()
        observe()
        listener()
        return binding.root
    }

    private fun initData() {
        viewModel = ViewModelProvider(this)[AccountLoginViewModel::class.java]

        val factory = Factory.instance()
        factory.setDebugMode(true, "Hello Linphone")
        core = factory.createCore(null, null, requireContext())
    }

    private fun observe() {
        registerObserverBaseEvent(viewModel, viewLifecycleOwner)
    }

    private fun listener() {
        binding.connect.setOnClickListener {
            it.isEnabled = true
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            val domain = binding.domain.text.toString()
            val displayName = binding.displayName.text.toString()
            val transportType = when (binding.transport.checkedRadioButtonId) {
                R.id.udp -> TransportType.Udp
                R.id.tcp -> TransportType.Tcp
                else -> TransportType.Tls
            }
            LogUtil.wtf("%s - %s - %s - %s - %s", username, password, domain, displayName, transportType)

            val authInfo = Factory.instance()
                .createAuthInfo(username, null, password, null, null, domain, null)

            val accountParams = core.createAccountParams()
            val identity = Factory.instance().createAddress("sip:$username@$domain")
            accountParams.identityAddress = identity

            val address = Factory.instance().createAddress("sip:$domain")
            address?.transport = transportType
            accountParams.serverAddress = address
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
            core.start()

            dataManager.mPreferenceHelper.username = username
            dataManager.mPreferenceHelper.password = password
            dataManager.mPreferenceHelper.domain = domain
            dataManager.mPreferenceHelper.transportType = transportType
            //viewModel.createAccountAndAuthInfo(username, password, domain, displayName, transportType)
            requireActivity().startActivity(DialerActivity.newIntent(requireContext()))
        }
    }
}