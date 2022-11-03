package io.github.takusan23.speedwatch.tool

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionTool {

    /**
     * 必要な権限がある場合はtrue
     *
     * @param context [Context]
     */
    fun isGrantedLocationPermission(context: Context): Boolean {
        val permissionList = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionList.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
    }

}