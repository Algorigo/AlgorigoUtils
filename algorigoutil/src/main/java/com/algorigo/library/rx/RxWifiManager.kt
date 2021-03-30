package com.algorigo.library.rx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

object RxWifiManager {

    class WifiNetworkAddFailed : Throwable("addNetwork return -1")

    fun scan(context: Context, only24GHz: Boolean = true): Single<List<ScanResult>> {
        val subject = PublishSubject.create<List<ScanResult>>()
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(con: Context?, intent: Intent?) {
                if (intent?.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                    var result = wifiManager.scanResults
                    if (only24GHz) {
                        result = result.filter { it.frequency <= 2500 }
                    }
                    subject.onNext(result)
                    subject.onComplete()
                }
            }
        }
        var loop = true
        return subject
            .firstOrError()
            .doOnSubscribe {
                context.registerReceiver(receiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
                if (!wifiManager.isWifiEnabled) {
                    wifiManager.isWifiEnabled = true
                }
                while (loop && !wifiManager.startScan()) {
                    Thread.sleep(5000)
                }
            }
            .doFinally {
                loop = false
                context.unregisterReceiver(receiver)
            }
    }

    fun connect(context: Context, ssid: String, password: String): Completable {
        val subject = PublishSubject.create<Int>()
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifiManager.connectionInfo.ssid.equals("\"$ssid\"")) {
            return Completable.complete()
        }

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(con: Context?, intent: Intent?) {
                if (intent?.action == WifiManager.NETWORK_STATE_CHANGED_ACTION) {
                    val networkInfo = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
                    if (networkInfo?.detailedState == NetworkInfo.DetailedState.CONNECTED && networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                        val info = wifiManager.connectionInfo
                        if (info.ssid.equals("\"$ssid\"") && !subject.hasComplete() && !subject.hasThrowable() && subject.hasObservers()) {
                            subject.onComplete()
                        }
                    }
                }
            }
        }

        return subject
            .ignoreElements()
            .doOnSubscribe {
                context.registerReceiver(receiver, IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION))
                if (!wifiManager.isWifiEnabled) {
                    wifiManager.isWifiEnabled = true
                }

                val wifiConfig = WifiConfiguration().apply {
                    SSID = "\"$ssid\""
                    if (password.isNotEmpty()) {
                        preSharedKey = "\"$password\""
                        status = WifiConfiguration.Status.ENABLED
                        allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
                        allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
                        allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
                        allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
                        allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
                    } else {
                        allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                    }
                }
                var netId = wifiManager.addNetwork(wifiConfig)
                if (netId < 0) {
                    val network = try {
                        if (password.isNotEmpty()) {
                            wifiManager.configuredNetworks.filter {
                                it.SSID.equals("\"$ssid\"") && it.preSharedKey != null
                            }.lastOrNull()
                        } else {
                            wifiManager.configuredNetworks.filter {
                                it.SSID.equals("\"$ssid\"") && it.preSharedKey == null
                            }.lastOrNull()
                        }
                    } catch (e: SecurityException) {
                        null
                    }
                    netId = network?.networkId ?: -1
                }

                if (netId < 0) {
                    throw WifiNetworkAddFailed()
                }

                var result = wifiManager.disconnect()
                result = result and wifiManager.enableNetwork(netId, true)
                result = result and wifiManager.reconnect()
                if (!result) {
                    throw WifiNetworkAddFailed()
                }
            }
            .doFinally {
                context.unregisterReceiver(receiver)
            }
    }

    fun remove(context: Context, ssid: String): Completable {
        return Completable.create {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val networks = try {
                wifiManager.configuredNetworks.filter {
                    it.SSID.equals("\"$ssid\"")
                }
            } catch (e: SecurityException) {
                it.onError(e)
                return@create
            }

            var result = wifiManager.disconnect()
            for (network in networks) {
                result = result and wifiManager.removeNetwork(network.networkId)
            }
            result = result and wifiManager.reconnect()
            if (!result) {
                it.onError(WifiNetworkAddFailed())
            } else {
                it.onComplete()
            }
        }
    }
}