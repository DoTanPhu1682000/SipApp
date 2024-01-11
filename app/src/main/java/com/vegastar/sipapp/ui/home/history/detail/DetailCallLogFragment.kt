package com.vegastar.sipapp.ui.home.history.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vegastar.sipapp.component.base.BaseFragment
import com.vegastar.sipapp.databinding.FragmentDetailCallLogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailCallLogFragment : BaseFragment() {
    private lateinit var binding: FragmentDetailCallLogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailCallLogBinding.inflate(inflater, container, false)
        return binding.root
    }
}