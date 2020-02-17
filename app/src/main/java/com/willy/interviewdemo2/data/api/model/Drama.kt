package com.willy.interviewdemo2.data.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

// 我的 Blog 文章 SerializedName: http://0rz.tw/jU2Gb
data class Drama(
    // ID
    @SerializedName("drama_id")
    val dramaId: Int,
    // 名稱
    val name: String,
    // 總觀看次數
    @SerializedName("total_views")
    val totalViews: Long,
    // 出版日期
    @SerializedName("created_at")
    val createdAt: String,
    // 縮圖 URL
    val thumb: String,
    // 評分
    val rating: Float
) : Serializable