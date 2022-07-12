package com.algorigo.algorigoutils

import android.Manifest
import android.net.wifi.ScanResult
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.algorigo.library.rx.RxWifiManager
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_wifi.*

class WifiActivity : AppCompatActivity() {

    private var scanResult: ScanResult? = null
    private val wifiAdapter = WifiAdapter(object : WifiAdapter.OnItemClickListener {
        override fun onItemClick(scanResult: ScanResult) {
            this@WifiActivity.scanResult = scanResult
            ssidEdit.setText(scanResult.SSID)
            passwordEdit.setText("")
        }
    })
    private var connectDisposable: Disposable? = null
    private var checkWifiConnectedDisposable: Disposable? = null

    private lateinit var rxPermissions: RxPermissions

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi)

        checkWifiConnected()
        rxPermissions = RxPermissions(this)

        wifiList.adapter = wifiAdapter

        scanBtn.setOnClickListener {
            scan()
        }
        connectBtn.setOnClickListener {
            scanResult?.let {
                connect(it)
            }
        }
        removeBtn.setOnClickListener {
            scanResult?.let {
                remove(it)
            }
        }
    }

    override fun onDestroy() {
        checkWifiConnectedDisposable?.dispose()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkWifiConnected() {
        if (checkWifiConnectedDisposable != null) {
            return
        }

        checkWifiConnectedDisposable = RxWifiManager
            .checkWifiConnectedObservable(this@WifiActivity)
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                checkWifiConnectedDisposable = null
            }
            .subscribe({ connectivity ->
                when (connectivity) {
                    RxWifiManager.Connectivity.CONNECTED -> {
                        Toast
                            .makeText(this@WifiActivity, "wifi connection is connected", Toast.LENGTH_SHORT)
                            .show()
                    }
                    RxWifiManager.Connectivity.DISCONNECTED -> {
                        Toast
                            .makeText(this@WifiActivity, "wifi connection is disconnected", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> throw IllegalArgumentException("connectivity is not found")
                }
            }, {
                Log.e("!!!", "error", it)
            })
    }

    private fun scan() {
        rxPermissions.requestEachCombined(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            .firstOrError()
            .flatMap { permission ->
                if(permission.granted) {
                    RxWifiManager.scan(this, false)
                } else {
                    Single.error(IllegalStateException("permission is not granted"))
                }
            }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("!!!", "onScan:${it.map { it.SSID }.toTypedArray().contentToString()}")
                    wifiAdapter.setData(it)
                }, {
                    Log.e("!!!", "error", it)
                })
    }

    private fun connect(scanResult: ScanResult) {
        if (connectDisposable != null) {
            return
        }

        connectDisposable = rxPermissions.requestEachCombined(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            .firstOrError()
            .flatMapObservable { permission ->
                if(permission.granted) {
                    RxWifiManager.connectWifi(this, scanResult.SSID, passwordEdit.text.toString())
                } else {
                    Observable.error(IllegalStateException("permission is not granted"))
                }
            }
                .doFinally {
                    connectDisposable = null
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("!!!", "onConnect")
                }, {
                    Log.e("!!!", "error4", it)
                })
    }

    private fun remove(scanResult: ScanResult) {
        connectDisposable?.dispose()
    }
}
