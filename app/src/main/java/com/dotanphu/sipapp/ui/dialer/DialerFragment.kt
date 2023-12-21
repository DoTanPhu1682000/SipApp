package com.dotanphu.sipapp.ui.dialer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dotanphu.sipapp.component.base.BaseFragment
import com.dotanphu.sipapp.data.DataManager
import com.dotanphu.sipapp.databinding.FragmentDialerBinding
import com.dotanphu.sipapp.ui.call.OutgoingCallActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

    @Inject
    lateinit var dataManager: DataManager

    private lateinit var binding: FragmentDialerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDialerBinding.inflate(inflater, container, false)
        listener()
        return binding.root
    }

    private fun listener() {
        binding.number0.setOnClickListener { appendToEditText("0") }
        binding.number1.setOnClickListener { appendToEditText("1") }
        binding.number2.setOnClickListener { appendToEditText("2") }
        binding.number3.setOnClickListener { appendToEditText("3") }
        binding.number4.setOnClickListener { appendToEditText("4") }
        binding.number5.setOnClickListener { appendToEditText("5") }
        binding.number6.setOnClickListener { appendToEditText("6") }
        binding.number7.setOnClickListener { appendToEditText("7") }
        binding.number8.setOnClickListener { appendToEditText("8") }
        binding.number9.setOnClickListener { appendToEditText("9") }
        binding.numberStar.setOnClickListener { appendToEditText("*") }
        binding.numberHash.setOnClickListener { appendToEditText("#") }

        binding.bCall.setOnClickListener {
            val phone = binding.edtSipUriInput.text.toString()
            requireActivity().startActivity(OutgoingCallActivity.newIntent(requireContext(), phone))
        }
        binding.imgDelete.setOnClickListener {
            val currentText = binding.edtSipUriInput.text.toString()
            if (currentText.isNotEmpty()) {
                val newText = currentText.substring(0, currentText.length - 1)
                binding.edtSipUriInput.setText(newText)
                binding.edtSipUriInput.setSelection(newText.length) // Đặt con trỏ ở cuối văn bản
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun appendToEditText(text: String) {
        val currentText = binding.edtSipUriInput.text.toString()
        binding.edtSipUriInput.setText(currentText + text)
    }
}