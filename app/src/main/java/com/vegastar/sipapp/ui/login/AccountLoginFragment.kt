package com.vegastar.sipapp.ui.login

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.utils.LogUtil
import com.vegastar.sipapp.AppConfig.ADDRESS_SIP
import com.vegastar.sipapp.R
import com.vegastar.sipapp.component.base.BaseFragment
import com.vegastar.sipapp.component.dialog.ConfirmDialog
import com.vegastar.sipapp.component.listener.OnDialogButtonClickListener
import com.vegastar.sipapp.data.DataManager
import com.vegastar.sipapp.databinding.FragmentAccountLoginBinding
import com.vegastar.sipapp.ui.home.MainActivity
import com.vegastar.sipapp.utils.core.CoreHelper
import com.vegastar.sipapp.utils.core.CoreHelperListener
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

            //CoreHelper.getInstance(requireContext())?.start()
            CoreHelper.getInstance(requireContext())?.login()
            CoreHelper.getInstance(requireContext())?.listener = this
        }

        viewModel.onLoginFailed.observe(viewLifecycleOwner) {
            hideProgress()
        }
    }

    private fun listener() {
        binding.imgShowPassword.setOnClickListener {
            if (binding.password.transformationMethod is PasswordTransformationMethod) {
                binding.password.transformationMethod = null
                binding.imgShowPassword.setImageDrawable(ContextCompat.getDrawable(getBaseContext()!!, R.drawable.ic_pw_eye))
            } else {
                binding.password.transformationMethod = PasswordTransformationMethod()
                binding.imgShowPassword.setImageDrawable(ContextCompat.getDrawable(getBaseContext()!!, R.drawable.ic_pw_eye_block))
            }
        }

        binding.imgAvatar.setOnClickListener {
//            val fcmToken = dataManager.mPreferenceHelper.fcmToken
//            val d: ConfirmDialog = ConfirmDialog.newInstance("Thông báo", fcmToken)
//            d.setCanceledOnTouchOutside(false)
//            d.isCancelable = false
//            d.setOnDialogButtonClickListener(object : OnDialogButtonClickListener {
//                override fun onPositiveButtonClick(dialog: Dialog?) {
//                    val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
//
//                    clipboard?.let {
//                        val clip = ClipData.newPlainText("FCM Token", fcmToken)
//                        it.setPrimaryClip(clip)
//
//                        Toast.makeText(context, "FCM Token đã được sao chép", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                    dialog?.cancel()
//                }
//
//                override fun onNegativeButtonClick(dialog: Dialog?) {
//                    dialog?.cancel()
//                }
//            })
//            try {
//                d.show(childFragmentManager, null)
//            } catch (e: Exception) {
//                LogUtil.e(e.message)
//            }
        }

        binding.connect.setOnClickListener {
            showProgress()
            it.isEnabled = true
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            val domain = ADDRESS_SIP
            val transportType = TransportType.Udp
            LogUtil.wtf("%s - %s - %s - %s", username, password, domain, transportType)

            viewModel.doLogin(requireContext(), username, password)
        }
    }

    override fun onRegistrationStateChanged(isSuccessful: Boolean) {
        if (isSuccessful) {
            hideProgress()
            requireActivity().startActivity(MainActivity.newIntent(requireContext()))
        } else {
            hideProgress()
            toastError(R.string.status_notice)
        }
    }
}