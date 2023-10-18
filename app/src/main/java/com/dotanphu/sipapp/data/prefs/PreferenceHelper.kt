package com.dotanphu.sipapp.data.prefs

import com.dotanphu.sipapp.data.model.response.Login

interface PreferenceHelper {

    /*----------------------------------[APP]-----------------------------------------------------*/
    fun logout()
    val userKey: String?
    val token: String?
    val refreshToken: String?
    val refreshTokenExpiresTime: Long
    val isLogin: Boolean
//    var fCMToken: String?

    /*----------------------------------[MEDIHOME]------------------------------------------------*/
    fun saveLoginInfo(login: Login)
    fun clearUserData()
    var loginPhone: String?
//    val loginType: String?
//    var isStringeeTokenRegistered: Boolean

    /*----------------------------------[COMMON]--------------------------------------------------*/
//    fun setCommonSetting(list: List<News>)
//    fun getCommonSetting(key: String)
}