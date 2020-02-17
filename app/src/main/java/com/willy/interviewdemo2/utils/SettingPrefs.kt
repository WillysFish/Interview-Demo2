package com.willy.interviewdemo2.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson

class SettingPrefs private constructor(context: Context) {

    companion object {
        // 是否開啟過 APP 了
        const val DB_KEY_LAUNCHED = "DB_KEY_LAUNCHED"
        // 最後搜尋的關鍵字
        const val DB_KEY_LAST_SEARCH = "DB_KEY_LAST_SEARCH"

        // For Singleton instantiation
        @Volatile
        private var instance: SettingPrefs? = null

        fun getInstance(context: Context): SettingPrefs {
            return instance ?: synchronized(this) {
                instance ?: SettingPrefs(context)
            }
        }
    }

    private val prefs: SharedPreferences = context.getSharedPreferences("db", MODE_PRIVATE)

    fun setObject(keyName: String, value: Any) = setString(keyName, Gson().toJson(value))

    /**
     *  我的 Blog 文章 Kotlin 泛型: http://0rz.tw/h7vuB
     */
    inline fun <reified T> getObject(keyName: String): T? =
        Gson().fromJson(getString(keyName), T::class.java)

    fun setString(keyName: String, value: String) {
        try {
            val encryptValue = AESUtil.encrypt(value)
            prefs.edit().putString(keyName, encryptValue).apply()
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }

    }

    fun getString(keyName: String, default: String = ""): String {
        var uid = prefs.getString(keyName, default) ?: default

        if (uid == default) return uid

        try {
            uid = AESUtil.decrypt(uid)
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }

        return uid
    }

    fun setBoolean(keyName: String, value: Boolean) {
        prefs.edit().putBoolean(keyName, value).apply()
    }

    fun getBoolean(keyName: String, default: Boolean = false): Boolean {
        return prefs.getBoolean(keyName, default)
    }

    fun setInt(keyName: String, value: Int) {
        prefs.edit().putInt(keyName, value).apply()
    }

    fun getInt(keyName: String, default: Int = 0): Int {
        return prefs.getInt(keyName, default)
    }

    fun setLong(keyName: String, value: Long) {
        prefs.edit().putLong(keyName, value).apply()
    }

    fun getLong(keyName: String, default: Long = 0): Long {
        return prefs.getLong(keyName, default)
    }

    fun removeData(keyName: String) {
        prefs.edit().remove(keyName).apply()
    }
}
