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
import com.dotanphu.sipapp.utils.core.CoreHelper
import com.dotanphu.sipapp.utils.core.CoreHelperListener
import com.utils.LogUtil
import dagger.hilt.android.AndroidEntryPoint
import org.linphone.core.TransportType
import javax.inject.Inject

@AndroidEntryPoint
class AccountLoginFragment : BaseFragment(), CoreHelperListener {
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

    private lateinit var binding: FragmentAccountLoginBinding
    private lateinit var viewModel: AccountLoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountLoginBinding.inflate(inflater, container, false)
        initData()
        observe()
        listener()
        return binding.root
    }

    private fun initData() {
        viewModel = ViewModelProvider(this)[AccountLoginViewModel::class.java]
    }

    private fun observe() {
        registerObserverBaseEvent(viewModel, viewLifecycleOwner)
        viewModel.onLoginSuccess.observe(viewLifecycleOwner) {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            dataManager.mPreferenceHelper.username = username
            dataManager.mPreferenceHelper.password = password

            val coreHelper = CoreHelper(requireContext())
            coreHelper.start()
            coreHelper.login(username, password)

            coreHelper.listener = this
        }
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

            viewModel.doLogin(requireContext(), username, password)
        }
    }

    override fun onRegistrationStateChanged(isSuccessful: Boolean) {
        if (isSuccessful) {
            requireActivity().startActivity(DialerActivity.newIntent(requireContext()))
        }
    }
}