package com.dotanphu.sipapp.data.prefs

import android.content.Context
import android.content.SharedPreferences
import com.dotanphu.sipapp.AppConfig
import javax.inject.Inject

class AppPreferenceHelper @Inject constructor(private val mContext: Context) : PreferenceHelper {
    companion object {
        private const val KEY_USER_KEY = "user_key"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_REFRESH_TOKEN_EXPIRES_TIME = "refresh_token_expires_time"
        const val KEY_FCM_TOKEN = "token_fcm"
        private const val KEY_LOGIN_PHONE = "login_phone"
        private const val KEY_LOGIN_TYPE = "login_type"
        private const val KEY_IS_STRINGEE_TOKEN_REGISTERED = "is_stringee_token_registered"
        private const val KEY_COMMON_SETTING = "common_setting"
    }

    private val mPref: SharedPreferences = mContext.getSharedPreferences(AppConfig.PREFERENCE_NAME, Context.MODE_PRIVATE)

    //Mã hóa thông tin với secret key
    private val isEncrypt = true

}