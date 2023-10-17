package com.dotanphu.sipapp.utils

import android.content.Context
import android.text.TextUtils
import com.androidnetworking.error.ANError
import com.exception.FileCompressException
import com.exception.MyException
import com.exception.ParseException
import com.exception.ValidException
import com.utils.ParseUtil
import com.widget.R
import javax.inject.Inject

class ErrorHandlerUtil
@Inject constructor(private val context: Context) {

    fun getApiErrorString(anError: ANError, vararg args: String): String {
        val code = anError.errorCode
        if (code == 0) return context.getString(R.string.error_unknown)
        val error = String.format("%s (code %s)", context.getString(R.string.error_unknown), code)
        val lengthArgs = args.size
        if (lengthArgs == 0) {
            val joBody = ParseUtil.createJSONObject(anError.errorBody)
            val msg = ParseUtil.getStringJson("message", joBody)
            return if (!TextUtils.isEmpty(msg)) msg else error
        }
        val joBody = ParseUtil.createJSONObject(anError.errorBody)
        var msg: String? = null
        if (lengthArgs == 1) msg = ParseUtil.getStringJson(args[0], joBody) else if (lengthArgs == 2) {
            val jo = ParseUtil.getJSONObject(args[0], joBody)
            msg = ParseUtil.getStringJson(args[1], jo)
        }
        return if (!TextUtils.isEmpty(msg)) msg.toString() else error
    }

    fun getOtherExceptionString(e: Throwable): String {
        if (e.message != null) return e.message!!
        if (e is ParseException) return context.getString(R.string.error_parse)
        if (e is ValidException) return context.getString(R.string.error_valid)
        if (e is FileCompressException) return context.getString(R.string.error_file_compress)
        return if (e is MyException) context.getString(R.string.error_general) else context.getString(R.string.error_unknown)
    }
}