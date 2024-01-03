package com.vegastar.sipapp.component.dialog

import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.vegastar.sipapp.component.base.BaseDialog
import com.vegastar.sipapp.component.listener.OnDialogSingleButtonClickListener
import com.vegastar.sipapp.databinding.DialogAlertBinding

class AlertDialog : BaseDialog() {
    private lateinit var binding: DialogAlertBinding
    private var title: String? = null
    private var content: String? = null
    private var mButtonText: String? = null
    private var resIcon = 0

    private var onDialogButtonClickListener: OnDialogSingleButtonClickListener? = null

    fun setOnDialogButtonClickListener(onDialogButtonClickListener: OnDialogSingleButtonClickListener) {
        this.onDialogButtonClickListener = onDialogButtonClickListener
    }

    companion object {
        private val KEY_RES_ICON = "KEY_RES_ICON"
        private val KEY_TITLE = "KEY_TITLE"
        private val KEY_CONTENT = "KEY_CONTENT"
        private val KEY_POSITIVE_BUTTON_TEXT = "KEY_POSITIVE_BUTTON_TEXT"

        fun newInstance(title: String?, content: String?): AlertDialog {
            val args = Bundle()
            val dialog = AlertDialog()
            dialog.setCanceledOnTouchOutside(false)
            args.putString(KEY_TITLE, title)
            args.putString(KEY_CONTENT, content)
            dialog.arguments = args
            return dialog
        }

        fun newInstance(title: String?, content: String?, @DrawableRes restIcon: Int, buttonText: String?): AlertDialog {
            val args = Bundle()
            val dialog = AlertDialog()
            dialog.setCanceledOnTouchOutside(false)
            args.putString(KEY_TITLE, title)
            args.putString(KEY_CONTENT, content)
            args.putString(KEY_POSITIVE_BUTTON_TEXT, buttonText)
            args.putInt(KEY_RES_ICON, restIcon)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogAlertBinding.inflate(inflater, container, false)
        receive()
        initData()
        listener()
        return binding.root
    }

    private fun receive() {
        val bundle = arguments
        if (bundle != null) {
            title = bundle.getString(KEY_TITLE)
            content = bundle.getString(KEY_CONTENT)
            mButtonText = bundle.getString(KEY_POSITIVE_BUTTON_TEXT)
            resIcon = bundle.getInt(KEY_RES_ICON)
        }
    }

    private fun initData() {
        if (!TextUtils.isEmpty(title)) {
            binding.tvTitle.text = title
            binding.tvTitle.visibility = View.VISIBLE
        } else binding.tvTitle.visibility = View.GONE

        if (!TextUtils.isEmpty(content)) {
            binding.tvContent.text = content
            binding.tvContent.visibility = View.VISIBLE
            binding.tvContent.movementMethod = LinkMovementMethod.getInstance()
        } else binding.tvContent.visibility = View.GONE

        if (!TextUtils.isEmpty(mButtonText)) binding.bPositive.text = mButtonText

        if (resIcon != 0) {
            binding.imgIcon.setImageResource(resIcon)
            binding.imgIcon.visibility = View.VISIBLE
        } else binding.imgIcon.visibility = View.GONE
    }

    private fun listener() {
        binding.bPositive.setOnClickListener(View.OnClickListener {
            if (!isClickable()) return@OnClickListener
            if (onDialogButtonClickListener != null) onDialogButtonClickListener!!.onPositiveButtonClick(dialog!!)
        })
    }
}