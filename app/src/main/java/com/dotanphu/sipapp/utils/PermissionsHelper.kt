package com.dotanphu.sipapp.utils

import android.Manifest
import android.os.Build
import com.dotanphu.sipapp.ui.home.MainActivity
import com.permissionx.guolindev.PermissionX

class PermissionsHelper(private val activity: MainActivity) {

    companion object {
        val appPermissions: Array<String> by lazy {
            if (Build.VERSION.SDK_INT >= 33) {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    // Manifest.permission.BLUETOOTH,  //remove in 31
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,  //31
                    Manifest.permission.READ_MEDIA_IMAGES,  //33,
                    Manifest.permission.POST_NOTIFICATIONS, //33
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else if (Build.VERSION.SDK_INT >= 31) {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    // Manifest.permission.BLUETOOTH,  //remove in 31
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,   //31
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, //remove in 33
                    Manifest.permission.READ_EXTERNAL_STORAGE, //remove in 33
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, //remove in 33
                    Manifest.permission.READ_EXTERNAL_STORAGE, //remove in 33
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

    fun requestAllPermissions(onPermissionGranted: () -> Unit, onPermissionDenied: () -> Unit) {
        val permissionsList = mutableListOf(*appPermissions)

        PermissionX.init(activity)
            .permissions(*permissionsList.toTypedArray()) // Chuyển danh sách quyền thành mảng
            .request { allGranted, _, _ ->
                if (allGranted) {
                    onPermissionGranted()
                } else {
                    onPermissionDenied()
                }
            }
    }
}