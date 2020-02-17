package com.willy.interviewdemo2.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "searchTag")
class SearchTagEntity(
    @PrimaryKey
    val keyword: String,
    // 搜尋次數
    var times: Long = 0,
    // 初次搜尋日期
    val createdAt: Date = Date(),
    // 最近搜尋日期
    var updatedAt: Date = Date()
)