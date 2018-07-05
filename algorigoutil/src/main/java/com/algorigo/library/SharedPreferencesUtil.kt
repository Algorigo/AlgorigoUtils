package com.algorigo.library

import android.content.Context
import java.lang.ref.WeakReference
import java.util.*

/**
 * SharedPreferencesUtil
 * Created by judh on 2018. 3. 14..
 */
object SharedPreferencesUtil {
    private const val STORAGE_KEY = "pref"
    const val SESSION_KEY = "cookies"
    private var weakReference: WeakReference<Context>? = null
    fun setContext(context: Context) {
        weakReference = WeakReference(context)
    }

    fun setString(key: String, value: String) {
        val editor = weakReference!!.get()!!.getSharedPreferences(STORAGE_KEY, 0).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String {
        return weakReference!!.get()!!.getSharedPreferences(STORAGE_KEY, 0).getString(key, "")
    }

    fun setInt(key: String, value: Int) {
        val editor = weakReference!!.get()!!.getSharedPreferences(STORAGE_KEY, 0).edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String): Int {
        return weakReference!!.get()!!.getSharedPreferences(STORAGE_KEY, 0).getInt(key, 0)
    }

    fun setBoolean(key: String, value: Boolean) {
        val editor = weakReference!!.get()!!.getSharedPreferences(STORAGE_KEY, 0).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return weakReference!!.get()!!.getSharedPreferences(STORAGE_KEY, 0).getBoolean(key, false)
    }

    fun setSet(key: String, value: Set<String>) {
        val editor = weakReference!!.get()!!.getSharedPreferences(STORAGE_KEY, 0).edit()
        editor.putStringSet(key, value)
        editor.apply()
    }

    fun getSet(key: String): Set<String> {
        return weakReference!!.get()!!.getSharedPreferences(STORAGE_KEY, 0).getStringSet(key, HashSet())
    }

    fun setString(prefKey: String, key: String, value: String) {
        val editor = weakReference!!.get()!!.getSharedPreferences(prefKey, 0).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(prefKey: String, key: String): String {
        return weakReference!!.get()!!.getSharedPreferences(prefKey, 0).getString(key, "")
    }

    fun setBoolean(prefKey: String, key: String, value: Boolean) {
        val editor = weakReference!!.get()!!.getSharedPreferences(prefKey, 0).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(prefKey: String, key: String): Boolean {
        return weakReference!!.get()!!.getSharedPreferences(prefKey, 0).getBoolean(key, false)
    }

    fun setInt(prefKey: String, key: String, value: Int) {
        val editor = weakReference!!.get()!!.getSharedPreferences(prefKey, 0).edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(prefKey: String, key: String): Int {
        return weakReference!!.get()!!.getSharedPreferences(prefKey, 0).getInt(key, 0)
    }
}