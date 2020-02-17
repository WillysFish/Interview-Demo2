package com.willy.interviewdemo2.utils

import android.text.TextUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.willy.interviewdemo2.BuildConfig

object Log {
    init {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .methodCount(5)
            .tag("WILLY_LOG")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    private val isProductionRelease =
        TextUtils.equals(BuildConfig.FLAVOR, "production") &&
                TextUtils.equals(BuildConfig.BUILD_TYPE, "release")

    @JvmStatic
    fun d(message: String, vararg args: Any) {
        Logger.d(message, *args)
    }

    @JvmStatic
    fun d(obj: Any?) {
        Logger.d(obj)
    }

    @JvmStatic
    fun i(message: String, vararg args: Any) {
        Logger.i(message, *args)
    }

    @JvmStatic
    fun e(message: String, vararg args: Any) {
        Logger.e(message, *args)
    }

    @JvmStatic
    fun e(throwable: Throwable?, message: String, vararg args: Any) {
        Logger.e(throwable, message, *args)
    }

    @JvmStatic
    fun json(jsonStr: String?) {
        // production 不印出機敏資料
        if (isProductionRelease) return
        Logger.json(jsonStr)
    }

    /**
     * 提供方法，需要在 production 印出資料的時候使用
     */
    @JvmStatic
    fun jsonProd(jsonStr: String?) {
        Logger.json(jsonStr)
    }

    /**
     * 帶有 debug 標記的 log
     */
    @JvmStatic
    fun rd(parametName: String, value: Any) {
        this.d("RDBG: $parametName = $value")
    }
}
