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

    fun setString(context: Context, key: String, value: String) {
        val editor = context.getSharedPreferences(STORAGE_KEY, 0).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(context: Context, key: String): String {
        return context.getSharedPreferences(STORAGE_KEY, 0).getString(key, "")!!
    }

    fun setInt(context: Context, key: String, value: Int) {
        val editor = context.getSharedPreferences(STORAGE_KEY, 0).edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(context: Context, key: String): Int {
        return context.getSharedPreferences(STORAGE_KEY, 0).getInt(key, 0)
    }

    fun setBoolean(context: Context, key: String, value: Boolean) {
        val editor = context.getSharedPreferences(STORAGE_KEY, 0).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(context: Context, key: String): Boolean {
        return context.getSharedPreferences(STORAGE_KEY, 0).getBoolean(key, false)
    }

    fun setSet(context: Context, key: String, value: Set<String>) {
        val editor = context.getSharedPreferences(STORAGE_KEY, 0).edit()
        editor.putStringSet(key, value)
        editor.apply()
    }

    fun getSet(context: Context, key: String): Set<String> {
        return context.getSharedPreferences(STORAGE_KEY, 0).getStringSet(key, HashSet())!!
    }

    fun setString(context: Context, prefKey: String, key: String, value: String) {
        val editor = context.getSharedPreferences(prefKey, 0).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(context: Context, prefKey: String, key: String): String {
        return context.getSharedPreferences(prefKey, 0).getString(key, "")!!
    }

    fun setBoolean(context: Context, prefKey: String, key: String, value: Boolean) {
        val editor = context.getSharedPreferences(prefKey, 0).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(context: Context, prefKey: String, key: String): Boolean {
        return context.getSharedPreferences(prefKey, 0).getBoolean(key, false)
    }

    fun setInt(context: Context, prefKey: String, key: String, value: Int) {
        val editor = context.getSharedPreferences(prefKey, 0).edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(context: Context, prefKey: String, key: String): Int {
        return context.getSharedPreferences(prefKey, 0).getInt(key, 0)
    }
}