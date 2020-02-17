package com.willy.interviewdemo2.data

/**
 * API 的統一回傳格式，將 Data 與 Error 合併回傳
 */
class DataResponse<T> private constructor(val rsData: T?, val error: ErrorCode) {
    constructor(rsData: T) : this(rsData, ErrorCode.NO_ERROR)
    constructor(error: ErrorCode) : this(null, error)
}
