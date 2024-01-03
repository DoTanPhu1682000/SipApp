package com.dotanphu.sipapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.databinding.FragmentCallLogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CallLogFragment : BaseFragment() {

    companion object {
        fun newInstance(): CallLogFragment {
            val args = Bundle()
            val fragment = CallLogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentCallLogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCallLogBinding.inflate(inflater, container, false)
        return binding.root
    }
}