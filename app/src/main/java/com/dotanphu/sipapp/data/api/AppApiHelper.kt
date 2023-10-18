package com.dotanphu.sipapp.data.api

import com.dotanphu.sipapp.data.prefs.AppPreferenceHelper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AppApiHelper @Inject constructor(val appPreferenceHelper: AppPreferenceHelper) : ApiHelper {

    val LOGIN_TYPE_OTP = "otp"
    val LOGIN_TYPE_QR = "qrcode"
    val LOGIN_TYPE_PASSWORD = ""
    val INDEX_INPUT_IOT = "IOT"
    val DEVICE_NAME = "SK23-001"

    private val APPLICATION_JSON = "application/json"
    private val APPLICATION_FORM_URL_ENCODE = "application/x-www-form-urlencoded"
    private val KEY_CONTENT_TYPE = "Content-Type"
    private val KEY_AUTHORIZATION = "Authorization"
    private val KEY_LANGUAGE = "lang"
    private val KEY_PAGE_SIZE = "page_size"
    private val KEY_PAGE_NUMBER = "page_number"

    override fun getString(): Single<String> {
        return Single.just("123")
    }
}