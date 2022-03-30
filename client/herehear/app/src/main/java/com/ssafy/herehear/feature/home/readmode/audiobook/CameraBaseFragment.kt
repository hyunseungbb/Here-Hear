package com.ssafy.herehear.feature.home.readmode.audiobook

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat


abstract class CameraBaseFragment : Fragment() {

    abstract fun permissionGranted(requestCode: Int)
    abstract fun permissionDenied(requestCode: Int)
    lateinit var attachedContext: Context

    fun requirePermissions(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted(requestCode)
        } else {
            val isAllPermissionGranted = permissions.all {
                attachedContext.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
            }
            if (isAllPermissionGranted) {
                permissionGranted(requestCode)
            } else {
                ActivityCompat.requestPermissions(requireActivity(), permissions, requestCode)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED}) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachedContext = context
    }
}