package com.willy.interviewdemo2.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.willy.interviewdemo2.data.db.entities.SearchTagEntity
import io.reactivex.Flowable

@Dao
interface SearchTagDao {
    /**
     * 新增一個 tag
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSearchTag(entity: SearchTagEntity)

    /**
     * 找到 tag of keyword
     */
    @Query("SELECT * FROM searchTag WHERE keyword LIKE :keyword")
    fun getTagByKeyword(keyword: String): SearchTagEntity?

    /**
     * 依 查詢次數 取得前三名 tag
     */
    @Query("SELECT * FROM searchTag ORDER BY times DESC LIMIT 3")
    fun getTagsByTimes(): Flowable<List<SearchTagEntity>>

    /**
     * 依 最近查詢 取得前十名 tag
     */
    @Query("SELECT * FROM searchTag ORDER BY updatedAt DESC LIMIT 10")
    fun getTagsByUpdateAt(): Flowable<List<SearchTagEntity>>
}