package com.willy.interviewdemo2.extension

import java.text.SimpleDateFormat
import java.util.*

// === Date ===
private val dateFormatLong = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.TAIWAN)
private val dateFormatShort = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)

/**
 * 將字串轉為時間格式
 */
fun Date.from_yyyyMMddHHmmss(value: String): Date = dateFormatLong.parse(value) ?: this

fun Date.from_yyyyMMdd(value: String): Date = dateFormatShort.parse(value) ?: this

/**
 * 將時間轉為字串格式
 */
val Date.yyyyMMddTHHmmss: String
    get() = dateFormatLong.format(this)

val Date.yyyyMMdd: String
    get() = dateFormatShort.format(this)



