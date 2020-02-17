package com.willy.interviewdemo2.extension

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

/**
 * 將 DP 轉為 PX
 */
fun Context.dpToPx(value: Int): Int = (value * resources.displayMetrics.density).toInt()

/**
 * 取得 View
 */
fun Context.inflate(resource: Int, root: ViewGroup?, attachToRoot: Boolean) =
    LayoutInflater.from(this).inflate(resource, root, attachToRoot)

/**
 * 收起軟鍵盤
 */
fun Context.hideKeyboard(view: View) {
    val inputManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}