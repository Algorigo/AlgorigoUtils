package com.algorigo.library.rx.permission

import io.reactivex.rxjava3.core.Completable

internal interface RxPermissions {

    class PermissionDenied(throwable: Throwable? = null) : Throwable("Permission Denied", throwable)

    fun requestPermissionCompletable(permissions: Array<String>, showRequestPermissionRationale: ((Array<String>) -> Completable)? = null): Completable

}