package com.dotanphu.sipapp.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Login : Serializable {
    @Expose
    @SerializedName("user_name")
    private val userName: String? = null

    @Expose
    @SerializedName("user_type")
    val userType: String? = null

    @Expose
    @SerializedName("user_key")
    val userKey: String? = null

    @Expose
    @SerializedName("scope")
    val scope: String? = null

    @Expose
    @SerializedName("expires_in")
    val expiresIn = 0

    @Expose
    @SerializedName("refresh_token")
    val refreshToken: String? = null

    @Expose
    @SerializedName("token_type")
    val tokenType: String? = null

    @Expose
    @SerializedName("access_token")
    val accessToken: String? = null

    @Expose
    @SerializedName("refresh_token_expires_time")
    val refreshTokenExpiresTime: Long = 0
}