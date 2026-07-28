package com.algorigo.library.rx

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class ShowRationaleException(permissions: List<String>) :
    Exception("Show rationale : ${permissions.joinToString(",")}")

class PermissionDeniedException(permissions: Set<String>) :
    Exception("Permission denied : ${permissions.joinToString(",")}")

class PermissionRequester {
    private val permissionResultRelay = PublishRelay.create<Set<String>>()
    lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    fun onPermissionResult(permissions: Set<String>) {
        permissionResultRelay.accept(permissions)
    }

    fun requestPermissionCompletable(
        context: Activity,
        vararg permissions: String
    ): Completable {
        return Single.fromCallable {
            permissions.filter {
                ContextCompat.checkSelfPermission(context, it) !=
                        android.content.pm.PackageManager.PERMISSION_GRANTED
            }
        }
            .flatMapCompletable { notGranted ->
                if (notGranted.isEmpty()) {
                    Completable.complete()
                } else if (notGranted.any {
                        ActivityCompat.shouldShowRequestPermissionRationale(context, it)
                    }) {
                    Completable.error(ShowRationaleException(notGranted))
                } else {
                    permissionResultRelay
                        .doOnSubscribe {
                            requestPermissionLauncher.launch(notGranted.toTypedArray())
                        }
                        .firstOrError()
                        .doOnSuccess {
                            if (it.isNotEmpty()) {
                                throw PermissionDeniedException(it)
                            }
                        }
                        .ignoreElement()
                }
            }
    }
}

fun ComponentActivity.registerPermissionRequester(permissionRequester: PermissionRequester) =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        permissionRequester.onPermissionResult(it.filter { !it.value }.keys)
    }
        .also {
            permissionRequester.requestPermissionLauncher = it
        }