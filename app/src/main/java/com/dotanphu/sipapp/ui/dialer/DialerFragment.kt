package com.dotanphu.sipapp.ui.dialer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.databinding.FragmentDialerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialerFragment : BaseFragment() {
    companion object {
        fun newInstance(): DialerFragment {
            val args = Bundle()
            val fragment = DialerFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentDialerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDialerBinding.inflate(inflater, container, false)
        return binding.root
    }
}