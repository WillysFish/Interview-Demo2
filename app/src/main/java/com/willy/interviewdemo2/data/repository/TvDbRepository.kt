package com.willy.interviewdemo2.data.repository

import com.google.gson.Gson
import com.willy.interviewdemo2.data.api.model.Drama
import com.willy.interviewdemo2.data.db.daos.DramaDao
import com.willy.interviewdemo2.data.db.daos.SearchTagDao
import com.willy.interviewdemo2.data.db.entities.DramaEntity
import com.willy.interviewdemo2.data.db.entities.SearchTagEntity
import com.willy.interviewdemo2.extension.toDrama
import com.willy.interviewdemo2.extension.toEntity
import com.willy.interviewdemo2.utils.Log
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import kotlin.collections.ArrayList

class TvDbRepository(private val dramaDao: DramaDao, private val searchTagDao: SearchTagDao) {

    /**
     * 將 tag 存進 DB, 已存在則更新 次數 & 時間
     */
    fun addTag(keyword: String) {
        if (keyword.trim().isEmpty()) return

        searchTagDao.getTagByKeyword(keyword)?.let {
            it.times += 1
            it.updatedAt = Date()
            searchTagDao.addSearchTag(it)
        } ?: searchTagDao.addSearchTag(SearchTagEntity(keyword))
    }

    /**
     * 將 Dramas 存進 DB
     */
    fun addDramas(list: List<Drama>) = dramaDao.addDramas(list.map { it.toEntity() })

    /**
     * 取得 Dramas
     */
    fun getDramasByOrder(orderBy: OrderBy): Single<ArrayList<Drama>> {
        return when (orderBy) {
            OrderBy.CREATE_AT -> dramaDao.getDramasByCreateAt().map { it.toDramaArrayList() }
            OrderBy.RATING -> dramaDao.getDramasByRating().map { it.toDramaArrayList() }
            else -> dramaDao.getDramasByViews().map { it.toDramaArrayList() }
        }
    }

    /**
     * 取得 Tags
     */
    fun getTagByOrder(orderBy: OrderBy): Flowable<ArrayList<String>> {
        return when (orderBy) {
            OrderBy.TIMES -> searchTagDao.getTagsByTimes().map { it.toKeywordArrayList() }
            else -> searchTagDao.getTagsByUpdateAt().map {
                Log.json(Gson().toJson(it))
                it
            }.map { it.toKeywordArrayList() }
        }
    }

    /**
     * 依 keyword + 總觀看人數排序取得 Dramas
     */
    fun getDramasByKeyword(keyword: String) =
        Observable.fromIterable(
            // 轉換為 keyword array, 逐筆 trigger
            keyword.trim().split(" ", "+").map { "%$it%" }
        ).flatMapSingle { searchWord ->
            // 發射查詢 by keyword
            dramaDao.getDramasByKeyword(searchWord).map { it.toDramaArrayList() }
        }.flatMap {
            // 取出結果
            Observable.fromIterable(it)
        }.toList( // 合併結果
        ).flatMap { list ->
            var sortList = ArrayList<SearchWeights>()
            // remove 重復 item，計算搜尋權重
            list.forEach {
                val weights = SearchWeights(it)
                if (!sortList.contains(weights)) {
                    sortList.add(weights)
                } else {
                    sortList[sortList.indexOf(weights)].count += 1
                }
            }

            val result = ArrayList(sortList.sortedWith(
                compareBy(
                    { it.count },
                    { it.views }
                )
            ).reversed().map { it.data })

            // 依權重 & 觀看數排序, 回傳查詢結果
            Single.just(result)
        }


    /**
     * DramaEntity List 轉 Drama ArrayList
     */
    private fun List<DramaEntity>.toDramaArrayList() = map { it.toDrama() } as ArrayList

    /**
     * SearchTagEntity List 轉 Keyword String ArrayList
     */
    private fun List<SearchTagEntity>.toKeywordArrayList() = map { it.keyword } as ArrayList


    /**
     * DB 排序選項
     */
    enum class OrderBy {
        VIEWS,
        CREATE_AT,
        RATING,
        UPDATE_AT,
        TIMES
    }

    /**
     * 排序權重 object
     */
    class SearchWeights(val data: Drama) {
        // 權重因子
        var count = 0
        val views = data.totalViews

        override fun hashCode(): Int {
            return data.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (other is SearchWeights) {
                return data == other.data
            }
            return super.equals(other)
        }
    }

}