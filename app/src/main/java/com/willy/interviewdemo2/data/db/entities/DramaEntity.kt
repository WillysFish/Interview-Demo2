package com.willy.interviewdemo2.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "dramas")
class DramaEntity(
    @PrimaryKey
    val dramaId: Int,
    // 名稱
    val name: String,
    // 總觀看次數
    val totalViews: Long,
    // 出版日期
    val createdAt: Date,
    // 縮圖 URL
    val thumb: String,
    // 評分
    val rating: Float
)