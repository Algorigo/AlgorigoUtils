package com.algorigo.library.rx.permission

import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.subjects.Subject

abstract class PermissionFragment : Fragment(), RxPermissions {

    private var requestCodeMap = mutableMapOf<Int, Subject<Int>>()

    override fun requestPermissionCompletable(permissions: Array<String>, showRequestPermissionRationale: ((Array<String>) -> Completable)?): Completable
            = PermissionMethods.requestPermissionCompletable(
        requireActivity(),
        requestCodeMap,
        permissions,
        this::requestPermissionsInner,
        showRequestPermissionRationale
    )

    private fun requestPermissionsInner(permissions: Array<String>, requestCode: Int)
            = PermissionMethods.requestPermissions(
        requireActivity(),
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