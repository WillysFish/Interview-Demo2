package com.willy.interviewdemo2.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.willy.interviewdemo2.data.db.entities.DramaEntity
import io.reactivex.Single

@Dao
interface DramaDao {
    /**
     * 新增一個 Drama
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDrama(entity: DramaEntity)

    /**
     * 新增多個 Dramas
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDramas(entities: List<DramaEntity>)

    /**
     * 依 觀看數 取得全部 Dramas
     */
    @Query("SELECT * FROM dramas ORDER BY totalViews DESC")
    fun getDramasByViews(): Single<List<DramaEntity>>

    /**
     * 依 上映日 取得全部 Dramas
     */
    @Query("SELECT * FROM dramas ORDER BY createdAt DESC")
    fun getDramasByCreateAt(): Single<List<DramaEntity>>

    /**
     * 依 評分 取得全部 Dramas
     */
    @Query("SELECT * FROM dramas ORDER BY rating DESC")
    fun getDramasByRating(): Single<List<DramaEntity>>

    /**
     * 取得符合 keyword 的 Dramas
     */
    @Query("SELECT * FROM dramas WHERE name LIKE :keyword ORDER BY totalViews DESC")
    fun getDramasByKeyword(keyword: String): Single<List<DramaEntity>>

}