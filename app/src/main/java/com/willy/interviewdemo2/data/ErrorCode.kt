package com.willy.interviewdemo2.data

import android.os.NetworkOnMainThreadException
import androidx.annotation.StringRes
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.willy.interviewdemo2.R
import com.willy.interviewdemo2.utils.Log
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 將會遇到的 Error 與相對應 show 給 user 的 msg 集中在這管理
 * 包含 Exception & API errorCode
 */
enum class ErrorCode(val code: Int, val msg: String, @StringRes val memo: Int) {
    // Internet Error
    BAD_GATEWAY(502, "Bad Gateway", R.string.BAD_GATEWAY),
    NOT_FOUND(404, "Not Found 未找到請求資源", R.string.NOT_FOUND),
    CONNECTION_TIMEOUT(408, "Request Timeout 請求逾時", R.string.CONNECTION_TIMEOUT),
    NETWORK_NOT_CONNECT(499, "Network Not Connect", R.string.NETWORK_NOT_CONNECT),
    UNEXPECTED_ERROR(700, "Unexpected Error", R.string.UNEXPECTED_ERROR),
    UNKNOWN_HOST(111700, "UnknownHostException", R.string.UNKNOWN_HOST),


    // Programme Error
    JSON_PARSE_EXCEPTION(8001, "JsonParseException", R.string.JSON_PARSE_EXCEPTION),
    NETWORK_ON_MAIN_THREAD(8002, "NetworkOnMainThreadException", R.string.NETWORK_ON_MAIN_THREAD),
    SOCKET_TIMEOUT(8003, "SocketTimeoutException", R.string.SOCKET_TIMEOUT),


    // TV Service Error
//    LOGIN_REQUIRED(1001, "login required", R.string.LOGIN_REQUIRED),


    // Custom Error
    DB_NOT_NEWEST(9001, "DB_NOT_NEWEST", R.string.DB_NOT_NEWEST),


    UNKNOWN(-1, "Unknown", R.string.UNKNOWN),
    NO_ERROR(0, "No Error", R.string.NO_ERROR);


    companion object {

        private val map = values().associateBy(ErrorCode::code)

        @JvmStatic
        fun findErrorByThrowable(e: Throwable): ErrorCode {
            Log.e(e, "ErrorCode")
            if (e is UnknownHostException) return UNKNOWN_HOST
            if (e is JsonParseException) return JSON_PARSE_EXCEPTION
            if (e is NetworkOnMainThreadException) return NETWORK_ON_MAIN_THREAD
            if (e is SocketTimeoutException) return SOCKET_TIMEOUT

            if (e is HttpException) {
                val code = e.code()
                val bodyStr = e.response()?.errorBody()?.string()
                if (bodyStr != null) {
                    val erCode = Gson().fromJson(bodyStr, JsonObject::class.java).get("code").asInt
                    return map["$code$erCode".toInt()] ?: UNKNOWN
                }
            }

            return UNKNOWN
        }
    }
}