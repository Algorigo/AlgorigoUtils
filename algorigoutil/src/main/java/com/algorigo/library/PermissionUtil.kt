package com.algorigo.library

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Process
import androidx.core.app.ActivityCompat

/**
 * PermissionUtil
 * Created by judh on 2018. 3. 14..
 */
object PermissionUtil {
    const val REQUEST_STORAGE = 1

    fun checkPermission(activity: Activity, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (!checkPermissions(activity, permission)) {
                requestExternalPermissions(activity, permissions)
                return false
            }
        }
        return true
    }

    private fun checkSelfPermission(context: Context, permission: String): Int {
        return context.checkPermission(permission, Process.myPid(), Process.myUid())
    }

    private fun checkPermissions(activity: Activity, permission: String): Boolean {
        val permissionResult = checkSelfPermission(activity, permission)

        return permissionResult == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        } else if (activity is ActivityCompat.OnRequestPermissionsResultCallback) {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                val grantResults = IntArray(permissions.size)

                val packageManager = activity.packageManager
                val packageName = activity.packageName

                val permissionCount = permissions.size

                for (i in 0 until permissionCount) {
                    grantResults[i] = packageManager.checkPermission(permissions[i], packageName)
                }


                (activity as ActivityCompat.OnRequestPermissionsResultCallback).onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    private fun requestExternalPermissions(activity: Activity, permissions: Array<String>) {
        requestPermissions(activity, permissions, REQUEST_STORAGE)
    }

    fun verifyPermission(grantResults: IntArray): Boolean {
        if (grantResults.isEmpty()) {
            return false
        }
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}