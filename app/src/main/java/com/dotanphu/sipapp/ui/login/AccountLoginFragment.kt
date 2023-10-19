package com.dotanphu.sipapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.databinding.FragmentAccountLoginBinding
import com.dotanphu.sipapp.ui.dialer.DialerActivity

class AccountLoginFragment : BaseFragment() {
    companion object {
        fun newInstance(): AccountLoginFragment {
            val args = Bundle()
            val fragment = AccountLoginFragment()
            fragment.arguments = args
            return fragment
        }
    }

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

    }

    private fun observe() {

    }

    private fun listener() {
        binding.connect.setOnClickListener {
            requireActivity().startActivity(DialerActivity.newIntent(requireContext()))
        }
    }
}