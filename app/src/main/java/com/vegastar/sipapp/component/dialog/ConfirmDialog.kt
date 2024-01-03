package com.vegastar.sipapp.component.dialog

import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.vegastar.sipapp.R
import com.vegastar.sipapp.component.base.BaseDialog
import com.vegastar.sipapp.component.listener.OnDialogButtonClickListener
import com.vegastar.sipapp.databinding.DialogConfirmBinding

class ConfirmDialog : BaseDialog(), View.OnClickListener {
    companion object {
        private const val KEY_RES_TITLE = "KEY_RES_TITLE"
        private const val KEY_RES_CONTENT = "KEY_RES_CONTENT"
        private const val KEY_RES_NEGATIVE = "KEY_RES_NEGATIVE"
        private const val KEY_RES_POSITIVE = "KEY_RES_POSITIVE"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_CONTENT = "KEY_CONTENT"
        private const val KEY_NEGATIVE = "KEY_NEGATIVE"
        private const val KEY_POSITIVE = "KEY_POSITIVE"

        fun newInstance(title: String, content: String): ConfirmDialog {
            val args = Bundle()
            val dialog = ConfirmDialog()
            dialog.setCanceledOnTouchOutside(false)
            args.putString(KEY_TITLE, title)
            args.putString(KEY_CONTENT, content)
            dialog.arguments = args
            return dialog
        }

        fun newInstance(@StringRes title: Int, @StringRes content: Int): ConfirmDialog {
            val args = Bundle()
            val dialog = ConfirmDialog()
            dialog.setCanceledOnTouchOutside(false)
            args.putInt(KEY_RES_TITLE, title)
            args.putInt(KEY_RES_CONTENT, content)
            dialog.arguments = args
            return dialog
        }

        fun newInstance(title: String, content: String, textPositive: String, textNegative: String): ConfirmDialog {
            val args = Bundle()
            val dialog = ConfirmDialog()
            dialog.setCanceledOnTouchOutside(false)
            args.putString(KEY_TITLE, title)
            args.putString(KEY_CONTENT, content)
            args.putString(KEY_NEGATIVE, textNegative)
            args.putString(KEY_POSITIVE, textPositive)
            dialog.arguments = args
            return dialog
        }

        fun newInstance(@StringRes title: Int, @StringRes content: Int, @StringRes textPositive: Int, @StringRes textNegative: Int): ConfirmDialog {
            val args = Bundle()
            val dialog = ConfirmDialog()
            dialog.setCanceledOnTouchOutside(false)
            args.putInt(KEY_RES_TITLE, title)
            args.putInt(KEY_RES_CONTENT, content)
            args.putInt(KEY_RES_NEGATIVE, textNegative)
            args.putInt(KEY_RES_POSITIVE, textPositive)
            dialog.arguments = args
            return dialog
        }
    }

    private lateinit var binding: DialogConfirmBinding

    private var onDialogButtonClickListener: OnDialogButtonClickListener? = null

    fun setOnDialogButtonClickListener(onDialogButtonClickListener: OnDialogButtonClickListener?) {
        this.onDialogButtonClickListener = onDialogButtonClickListener
    }

    private var textPositive: String? = null
    private var textNegative: String? = null
    private var title: String? = null
    private var content: String? = null
    private var resTitle = 0
    private var resContent = 0
    private var resNegative = 0
    private var resPositive = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogConfirmBinding.inflate(inflater, container, false)
        receive()
        initData()
        listener()
        return binding.root
    }

    private fun receive() {
        val bundle: Bundle? = arguments
        if (bundle != null) {
            title = bundle.getString(KEY_TITLE)
            content = bundle.getString(KEY_CONTENT)
            textPositive = bundle.getString(KEY_POSITIVE)
            textNegative = bundle.getString(KEY_NEGATIVE)
            resTitle = bundle.getInt(KEY_RES_TITLE)
            resContent = bundle.getInt(KEY_RES_CONTENT)
            resNegative = bundle.getInt(KEY_RES_NEGATIVE)
            resPositive = bundle.getInt(KEY_RES_POSITIVE)
        }
    }

    private fun initData() {
        binding.tvTitle.text = title
        binding.tvContent.text = content
        binding.tvContent.movementMethod = LinkMovementMethod.getInstance()
        if (!TextUtils.isEmpty(textPositive)) binding.bPositive.text = textPositive
        if (!TextUtils.isEmpty(textNegative)) binding.bNegative.text = textNegative
        if (resTitle != 0) binding.tvTitle.setText(resTitle)
        if (resContent != 0) binding.tvContent.setText(resContent)
        if (resNegative != 0) binding.bNegative.setText(resNegative)
        if (resPositive != 0) binding.bPositive.setText(resPositive)
    }

    private fun listener() {
        binding.bNegative.setOnClickListener(this)
        binding.bPositive.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.bNegative) {
            onDialogButtonClickListener?.onNegativeButtonClick(dialog)
        } else if (id == R.id.bPositive) {
            onDialogButtonClickListener?.onPositiveButtonClick(dialog)
        }
    }
}