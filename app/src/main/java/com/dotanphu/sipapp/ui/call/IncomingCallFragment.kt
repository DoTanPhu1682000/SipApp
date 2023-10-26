package com.dotanphu.sipapp.ui.call

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.databinding.FragmentIncomingCallBinding
import com.dotanphu.sipapp.utils.core.CoreHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class IncomingCallFragment : BaseFragment() {
    companion object {
        fun newInstance(): IncomingCallFragment {
            val args = Bundle()
            val fragment = IncomingCallFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var dataManager: DataManager

    private lateinit var binding: FragmentIncomingCallBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentIncomingCallBinding.inflate(inflater, container, false)
        initData()
        listener()
        return binding.root
    }

    private fun initData() {
        CoreHelper.getInstance(requireContext())?.start()
    }

    private fun listener() {
        binding.buttons.bHangup.setOnClickListener {
            CoreHelper.getInstance(requireContext())?.hangUpIncomingCall()
        }
        binding.buttons.bAnswer.setOnClickListener {
            CoreHelper.getInstance(requireContext())?.answer()
        }
    }
}