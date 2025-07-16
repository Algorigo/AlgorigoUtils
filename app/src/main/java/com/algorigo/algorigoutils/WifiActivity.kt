package com.algorigo.algorigoutils

import android.Manifest
import android.net.wifi.ScanResult
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.algorigo.algorigoutils.databinding.ActivityWifiBinding
import com.algorigo.library.rx.PermissionRequester
import com.algorigo.library.rx.RxWifiManager
import com.algorigo.library.rx.registerPermissionRequester
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.BufferedInputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WifiActivity : AppCompatActivity() {

    private var scanResult: ScanResult? = null
    private val wifiAdapter = WifiAdapter(object : WifiAdapter.OnItemClickListener {
        override fun onItemClick(scanResult: ScanResult) {
            this@WifiActivity.scanResult = scanResult
            binding.ssidEdit.setText(scanResult.SSID)
            binding.passwordEdit.setText("")
        }
    })
    private var connectDisposable: Disposable? = null
    private var checkWifiConnectedDisposable: Disposable? = null
    private lateinit var binding: ActivityWifiBinding

    private val permissionRequester = PermissionRequester()

    init {
        registerPermissionRequester(permissionRequester)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWifiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkWifiConnected()

        binding.wifiList.adapter = wifiAdapter

        binding.scanBtn.setOnClickListener {
            scan()
        }
        binding.connectBtn.setOnClickListener {
            scanResult?.let {
                connect(it)
            }
        }
        binding.removeBtn.setOnClickListener {
            scanResult?.let {
                remove(it)
            }
        }
        binding.testBtn.setOnClickListener {
            Single.fromCallable {
                val url = URL("https://naver.com")
                val connection = url.openConnection()
                val input = BufferedInputStream(connection.getInputStream())
                input?.readBytes()?.toString(Charsets.UTF_8) ?: ""
            }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Log.e("!!!", "onTest: ${it.substring(0..100)}")
                }, {
                    Log.e("!!!", "error", it)
                })
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
        permissionRequester.requestPermissionCompletable(this, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
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

        connectDisposable = permissionRequester.requestPermissionCompletable(this, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            .andThen(RxWifiManager.connectWifi(this, scanResult.SSID, binding.passwordEdit.text.toString()))
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
