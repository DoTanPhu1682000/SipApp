package com.vegastar.sipapp.component.listener

import android.app.Dialog

interface OnDialogButtonClickListener {
    fun onPositiveButtonClick(dialog: Dialog?)
    fun onNegativeButtonClick(dialog: Dialog?)
}