package com.dotanphu.sipapp.data.api

import io.reactivex.rxjava3.core.Single

interface ApiHelper {
    fun getString(): Single<String>
}