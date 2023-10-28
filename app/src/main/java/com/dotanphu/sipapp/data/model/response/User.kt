package com.dotanphu.sipapp.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class User : Serializable {
    @Expose
    @SerializedName("id")
    val id: String? = null

    @Expose
    @SerializedName("auth")
    val auth: String? = null

    @Expose
    @SerializedName("authid")
    val authid: String? = null

    @Expose
    @SerializedName("username")
    val username: String? = null

    @Expose
    @SerializedName("description")
    val description: String? = null

    @Expose
    @SerializedName("password")
    val password: String? = null

    @Expose
    @SerializedName("default_extension")
    val defaultExtension: String? = null

    @Expose
    @SerializedName("primary_group")
    val primaryGroup: String? = null

    @Expose
    @SerializedName("fname")
    val fName: String? = null

    @Expose
    @SerializedName("lname")
    val lName: String? = null

    @Expose
    @SerializedName("displayname")
    val displayName: String? = null

    @Expose
    @SerializedName("assigned")
    val assigned: List<String>? = null
}