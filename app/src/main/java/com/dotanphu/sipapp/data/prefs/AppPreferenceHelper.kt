package com.dotanphu.sipapp.data.prefs

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.dotanphu.sipapp.AppConfig
import com.dotanphu.sipapp.data.model.response.Login
import com.dotanphu.sipapp.utils.constant.StringConstant
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

    /*----------------------------------[APP]-----------------------------------------------------*/
    override fun logout() {
        //Xóa UserId FirebaseAnalytics
        //FirebaseAnalyticsHelper.logEvent(mContext, EventAnalytic.eve_logout)

        //Thực hiện xóa thông tin Login
        loginPhone = null
        //Xóa dữ liệu trong Share Preference
        clearUserData()
    }

    override val userKey: String?
        get() = mPref.getString(KEY_USER_KEY, "")

    override val token: String?
        get() = mPref.getString(KEY_ACCESS_TOKEN, "")

    override val refreshToken: String?
        get() = mPref.getString(KEY_REFRESH_TOKEN, "")

    override val refreshTokenExpiresTime: Long
        get() = mPref.getLong(KEY_REFRESH_TOKEN_EXPIRES_TIME, 0)

    override val isLogin: Boolean
        get() = !TextUtils.isEmpty(userKey) && !TextUtils.isEmpty(token) && !TextUtils.isEmpty(refreshToken)

    /*----------------------------------[MEDIHOME]------------------------------------------------*/
    override fun saveLoginInfo(login: Login) {
        val token: String = java.lang.String.format(StringConstant.FORMAT_TOKEN, login.accessToken)
        mPref.edit().putString(KEY_ACCESS_TOKEN, token).commit()
        mPref.edit().putString(KEY_REFRESH_TOKEN, login.refreshToken).commit()
        mPref.edit().putLong(KEY_REFRESH_TOKEN_EXPIRES_TIME, login.refreshTokenExpiresTime).commit()
        mPref.edit().putString(KEY_USER_KEY, login.userKey).commit()
    }

    override fun clearUserData() {
        mPref.edit().putString(KEY_LOGIN_PHONE, "").commit()
        mPref.edit().putString(KEY_REFRESH_TOKEN, "").commit()
        mPref.edit().putString(KEY_ACCESS_TOKEN, "").commit()
        mPref.edit().putString(KEY_USER_KEY, "").commit()
        mPref.edit().putLong(KEY_REFRESH_TOKEN_EXPIRES_TIME, 0).commit()
    }

    override var loginPhone: String?
        get() = mPref.getString(KEY_LOGIN_PHONE, null)
        set(value) {
            mPref.edit().putString(KEY_LOGIN_PHONE, value).commit()
        }
}