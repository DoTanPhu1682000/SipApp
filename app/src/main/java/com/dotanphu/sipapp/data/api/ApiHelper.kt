package com.dotanphu.sipapp.data.api

import com.dotanphu.sipapp.data.model.response.Login
import com.dotanphu.sipapp.data.model.response.User
import io.reactivex.rxjava3.core.Single
import org.json.JSONObject

interface ApiHelper {

    fun getUserInfo(): Single<Login>

    fun getListUsers(): Single<List<User>>

    fun sendNotificationFcmDirect(tokenFCM: String, title: String, body: String): Single<JSONObject>

    /*----------------------------------[ACCOUNT]-------------------------------------------------*/
    fun login(phone: String, password: String): Single<Login>
}