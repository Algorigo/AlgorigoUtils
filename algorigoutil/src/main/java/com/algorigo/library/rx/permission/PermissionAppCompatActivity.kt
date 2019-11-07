package com.algorigo.library.rx.permission

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Completable
import io.reactivex.subjects.Subject

abstract class PermissionAppCompatActivity : AppCompatActivity(),
    RxPermissions {

    private var requestCodeMap = mutableMapOf<Int, Subject<Int>>()

    override fun requestPermissionCompletable(permissions: Array<String>, showRequestPermissionRationale: ((Array<String>) -> Completable)?): Completable
        = PermissionMethods.requestPermissionCompletable(
        this,
        requestCodeMap,
        permissions,
        this::requestPermissionsInner,
        showRequestPermissionRationale
    )

    private fun requestPermissionsInner(permissions: Array<String>, requestCode: Int)
            = PermissionMethods.requestPermissions(
        this,
        permissions,
        requestCode,
        this::onRequestPermissionsResult
    )

    final override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (!PermissionMethods.onRequestPermissionsResult(
                requestCodeMap,
                requestCode,
                permissions,
                grantResults
            )
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}