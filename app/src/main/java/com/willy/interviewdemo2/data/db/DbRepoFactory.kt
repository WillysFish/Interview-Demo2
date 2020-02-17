package com.willy.interviewdemo2.data.db

import android.content.Context
import com.willy.interviewdemo2.data.repository.TvDbRepository

object DbRepoFactory {

    @Volatile
    private var tvRepo: TvDbRepository? = null

    fun getDramaRepo(context: Context): TvDbRepository {
        return tvRepo ?: synchronized(this) {
            val db = AppDatabase.getInstance(context)
            tvRepo ?: TvDbRepository(db.dramaDao(), db.searchTagDao()).also {
                tvRepo = it
            }
        }
    }
}