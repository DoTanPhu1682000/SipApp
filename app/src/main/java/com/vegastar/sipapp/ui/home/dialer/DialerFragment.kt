package com.vegastar.sipapp.ui.home.dialer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.utils.LogUtil
import com.vegastar.sipapp.component.base.BaseFragment
import com.vegastar.sipapp.data.DataManager
import com.vegastar.sipapp.data.model.response.User
import com.vegastar.sipapp.databinding.FragmentDialerBinding
import com.vegastar.sipapp.ui.call.OutgoingCallActivity
import com.vegastar.sipapp.ui.home.contact.ContactViewModel
import com.widget.ToastColor
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
    private lateinit var viewModel: ContactViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDialerBinding.inflate(inflater, container, false)
        initData()
        observe()
        listener()
        getData()
        return binding.root
    }

    private fun initData() {
        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]
    }

    private fun observe() {
        registerObserverBaseEvent(viewModel, viewLifecycleOwner)

        viewModel.onUsersSuccess.observe(viewLifecycleOwner) { listUser ->
            if (!checkLiveDataState(viewLifecycleOwner)) return@observe
            // Truyền danh sách người dùng vào AppPreferenceHelper
            val userList: List<User> = listUser
            dataManager.mPreferenceHelper.saveUserList(userList)
        }
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
            val storedUserList: List<User> = dataManager.mPreferenceHelper.getUserList()

            val filterUsers = filterUsersByPhoneNumber(storedUserList, phone)
            if (filterUsers.isNotEmpty()) {
                for (user in filterUsers) {
                    LogUtil.wtf("fcmToken: ${user.description}")
                    if (user.description!!.isNotEmpty()) {
                        requireActivity().startActivity(OutgoingCallActivity.newIntent(requireContext(), phone, user.displayName, user.description))
                    }
                }
            } else {
                ToastColor.error(requireContext(), "Số điện thoại không tồn tại")
            }
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

    private fun getData() {
        if (!isNetworkConnected) {
            return
        }

        viewModel.getListUsers()
    }

    @SuppressLint("SetTextI18n")
    private fun appendToEditText(text: String) {
        val currentText = binding.edtSipUriInput.text.toString()
        binding.edtSipUriInput.setText(currentText + text)
    }

    // Hàm để lọc danh sách người dùng theo số điện thoại
    private fun filterUsersByPhoneNumber(users: List<User>, phoneNumber: String): List<User> {
        return users.filter { it.username == phoneNumber }
    }
}