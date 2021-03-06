package com.algorigo.algorigoutils

import android.Manifest
import android.net.wifi.ScanResult
import android.os.Bundle
import android.util.Log
import com.algorigo.library.rx.RxWifiManager
import com.algorigo.library.rx.permission.PermissionAppCompatActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_wifi.*

class WifiActivity : PermissionAppCompatActivity() {

    private var scanResult: ScanResult? = null
    private val wifiAdapter = WifiAdapter(object : WifiAdapter.OnItemClickListener {
        override fun onItemClick(scanResult: ScanResult) {
            this@WifiActivity.scanResult = scanResult
            ssidEdit.setText(scanResult.SSID)
            passwordEdit.setText("")
        }
    })
    private var connectDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi)

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

    private fun scan() {
        requestPermissionCompletable(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
                .andThen(RxWifiManager.scan(this, false))
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

        connectDisposable = requestPermissionCompletable(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
                .andThen(RxWifiManager.connectWifi(this, scanResult.SSID, passwordEdit.text.toString()))
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
