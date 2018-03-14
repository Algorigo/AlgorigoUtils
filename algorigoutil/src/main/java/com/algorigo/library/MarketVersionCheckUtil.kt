package com.algorigo.library

import android.content.Context
import android.content.pm.PackageManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * MarketVersionCheckUtil
 * Created by judh on 2018. 3. 14..
 */
object MarketVersionCheckUtil {
    enum class MarketVersionCheckResult {
        VERSION_CHECK_OK,
        VERSION_CHECK_UPDATE,
        VERSION_CHECK_FAIL
    }

    private fun getMarketVersionFast(packageName: String): String? {
        var mData = ""
        var mVer: String?
        try {
            val mUrl = URL("https://play.google.com/store/apps/details?id=$packageName")
            val mConnection = mUrl.openConnection() as HttpURLConnection
            mConnection.connectTimeout = 5000
            mConnection.useCaches = false
            mConnection.doOutput = true
            if (mConnection.responseCode == HttpURLConnection.HTTP_OK) {
                val mReader = BufferedReader(InputStreamReader(mConnection.inputStream))
                while (true) {
                    val line = mReader.readLine() ?: break
                    mData += line
                }
                mReader.close()
            }
            mConnection.disconnect()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }

        val startToken = "softwareVersion\">"
        val endToken = "<"
        val index = mData.indexOf(startToken)
        if (index == -1) {
            mVer = null
        } else {
            mVer = mData.substring(index + startToken.length, index + startToken.length + 100)
            mVer = mVer.substring(0, mVer.indexOf(endToken)).trim { it <= ' ' }
        }
        return mVer
    }

    fun checkUpdate(context: Context): MarketVersionCheckResult {
        val storeVersion = getMarketVersionFast(context.packageName)

        return try {
            val deviceVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            if (storeVersion != null) {
                if (storeVersion > deviceVersion) {
                    MarketVersionCheckResult.VERSION_CHECK_UPDATE
                } else {
                    MarketVersionCheckResult.VERSION_CHECK_OK
                }
            } else {
                MarketVersionCheckResult.VERSION_CHECK_FAIL
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            MarketVersionCheckResult.VERSION_CHECK_FAIL
        }
    }
}