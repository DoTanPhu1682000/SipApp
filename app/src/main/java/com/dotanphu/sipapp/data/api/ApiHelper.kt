package com.dotanphu.sipapp.data.api

import com.dotanphu.sipapp.data.model.response.Login
import com.dotanphu.sipapp.data.model.response.User
import io.reactivex.rxjava3.core.Single

interface ApiHelper {

    fun getUserInfo(): Single<Login>

    fun getListUsers(): Single<List<User>>

    /*----------------------------------[ACCOUNT]-------------------------------------------------*/
    fun login(phone: String, password: String): Single<Login>
}