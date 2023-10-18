package com.dotanphu.sipapp.data.api

import com.dotanphu.sipapp.data.model.response.Login
import io.reactivex.rxjava3.core.Single

interface ApiHelper {

    fun getUserInfo(): Single<Login>

    /*----------------------------------[ACCOUNT]-------------------------------------------------*/
    fun login(phone: String, password: String): Single<Login>
}