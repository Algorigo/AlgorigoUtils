package com.algorigo.library.rx.permission

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import androidx.core.app.ActivityCompat
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

internal object PermissionMethods {

    internal fun requestPermissionCompletable(activity: Activity, requestCodeMap: MutableMap<Int, Subject<Int>>, permissions: Array<String>, requestPermission: (Array<String>, Int) -> Unit, showRequestPermissionRationale: ((Array<String>) -> Completable)? = null): Completable {
        if (permissions.size == 0) {
            return Completable.error(IllegalArgumentException("permission is empty"))
        }

        val notPermitted = mutableListOf<String>()

        for (permission in permissions) {
            if (activity.checkPermission(permission, Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED) {
                notPermitted.add(permission)
            }
        }

        if (notPermitted.size == 0) {
            return Completable.complete()
        } else {
            val subject = PublishSubject.create<Int>()
            val requestCode =
                generateNewRequestCode(
                    requestCodeMap
                )
            requestCodeMap.put(requestCode, subject)
            return subject
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (showRequestPermissionRationale == null) {
                        requestPermission(permissions, requestCode)
                    } else {
                        val rationales = mutableListOf<String>()
                        for (permission in notPermitted) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                                rationales.add(permission)
                            }
                        }
                        if (rationales.isEmpty()) {
                            requestPermission(permissions, requestCode)
                        } else {
                            showRequestPermissionRationale(rationales.toTypedArray())
                                .subscribe({
                                    requestPermission(permissions, requestCode)
                                }, {
                                    throw RxPermissions.PermissionDenied(
                                        it
                                    )
                                })
                        }
                    }
                }
                .doFinally {
                    requestCodeMap.remove(requestCode)
                }
                .ignoreElements()
        }
    }

    internal fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int, onRequestPermissionsResult: (Int, Array<out String>, IntArray) -> Unit) {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        } else {
            val grantResults = IntArray(permissions.size)

            val permissionCount = permissions.size

            for (i in 0 until permissionCount) {
                grantResults[i] = activity.packageManager.checkPermission(permissions[i], activity.packageName)
            }

            onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    internal fun onRequestPermissionsResult(requestCodeMap: Map<Int, Subject<Int>>, requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        if (requestCodeMap.containsKey(requestCode)) {
            val subject = requestCodeMap.getValue(requestCode)
            if (verifyPermission(grantResults)) {
                // 요청한 권한을 얻었으므로 원하는 메소드를 사용
                subject.onComplete()
            } else {
                subject.onError(RxPermissions.PermissionDenied())
            }
            return true
        } else {
            return false
        }
    }

    @Synchronized
    private fun generateNewRequestCode(requestCodeMap: Map<Int, Subject<Int>>): Int {
        var randomNumber: Int
        do {
            randomNumber = Math.round(Math.random() * 1000).toInt()
        } while (requestCodeMap.containsKey(randomNumber))

        return randomNumber
    }

    private fun verifyPermission(grantResuts: IntArray): Boolean {
        if (grantResuts.isEmpty()) {
            return false
        }
        for (result in grantResuts) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}