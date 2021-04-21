package com.algorigo.library.rx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.net.wifi.*
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject

object RxWifiManager {

    class WifiNetworkAddFailed : Throwable("addNetwork return -1")
    class RemoveWifiOwnerException : Throwable("Wifi is not added by this application")

    enum class Connectivity {
        CONNECTED,
    }

    private val LOG_TAG = RxWifiManager::class.java.simpleName

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
                context.applicationContext.registerReceiver(receiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
                if (!wifiManager.isWifiEnabled) {
                    wifiManager.isWifiEnabled = true
                }
                while (loop && !wifiManager.startScan()) {
                    Thread.sleep(5000)
                }
            }
            .doFinally {
                loop = false
                context.applicationContext.unregisterReceiver(receiver)
            }
    }


    fun connectWifi(context: Context, ssid: String, password: String): Observable<Connectivity> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connectWifiOverQ(context, ssid, password)
        } else {
            var subject = PublishSubject.create<Connectivity>()
            connect(context, ssid, password)
                    .andThen(Single.just(Connectivity.CONNECTED))
                    .toObservable()
                    .concatWith(subject)
                    .doFinally {
                        remove(context, ssid)
                                .subscribe({
                                    Log.i(LOG_TAG, "Remove Successfully")
                                }, {
                                    Log.e(LOG_TAG, "", it)
                                })
                    }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun connectWifiOverQ(context: Context, ssid: String, password: String): Observable<Connectivity> {
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return if (connectivityManager != null) {
            val subject = PublishSubject.create<Connectivity>()
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Log.e("!!!", "onAvailable:$network")
                    subject.onNext(Connectivity.CONNECTED)
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    Log.e("!!!", "onLosing:$network")
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Log.e("!!!", "onLost:$network")
                    subject.onError(WifiNetworkAddFailed())
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    Log.e("!!!", "onUnavailable")
                    subject.onError(WifiNetworkAddFailed())
                }

                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    Log.e("!!!", "onCapabilitiesChanged:$network, $networkCapabilities")
                }

                override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                    super.onLinkPropertiesChanged(network, linkProperties)
                    Log.e("!!!", "onLinkPropertiesChanged:$network, $linkProperties")
                }

                override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                    super.onBlockedStatusChanged(network, blocked)
                    Log.e("!!!", "onBlockedStatusChanged:$network, $blocked")
                }
            }

            val specifier = WifiNetworkSpecifier.Builder()
                    .setSsid(ssid)
                    .setWpa2Passphrase(password)
                    .build()
            val request = NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .setNetworkSpecifier(specifier)
                    .build()

            subject
                    .doOnSubscribe {
                        connectivityManager.requestNetwork(request, callback)
                    }
                    .doFinally {
                        connectivityManager.unregisterNetworkCallback(callback)
                    }
        } else {
            Observable.error(NullPointerException())
        }
    }

    @Suppress("DEPRECATION")
    @Deprecated("deprecated in Android Q")
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
                context.applicationContext.registerReceiver(receiver, IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION))
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
                context.applicationContext.unregisterReceiver(receiver)
            }
    }

    @Suppress("DEPRECATION")
    @Deprecated("deprecated in Android Q")
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

            val disconnectResult = wifiManager.disconnect()
            if (!disconnectResult) {
                it.onError(RemoveWifiOwnerException())
                return@create
            }

            var removeResult = true
            for (network in networks) {
                removeResult = removeResult and wifiManager.removeNetwork(network.networkId)
            }

            val reconnectResult = wifiManager.reconnect()
            if (!reconnectResult) {
                it.onError(WifiNetworkAddFailed())
                return@create
            }

            if (!removeResult) {
                it.onError(RemoveWifiOwnerException())
                return@create
            }

            it.onComplete()
        }
    }
}