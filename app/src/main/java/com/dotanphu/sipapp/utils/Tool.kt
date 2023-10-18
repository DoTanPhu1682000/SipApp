package com.dotanphu.sipapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.utils.LogUtil

object Tool {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val networkInfo = connectivityManager.activeNetworkInfo
            // if no network is available networkInfo will be null
            return networkInfo != null && networkInfo.isConnected
        }

        //For >= Android Q
        try {
            val networks = connectivityManager.allNetworks
            if (networks.size > 0) {
                for (network in networks) {
                    val nc = connectivityManager.getNetworkCapabilities(network)
                    if (nc!!.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) return true
                }
            }
        } catch (e: Exception) {
            LogUtil.e(e.message)
        }
        return false
    }
}