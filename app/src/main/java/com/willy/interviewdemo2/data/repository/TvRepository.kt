package com.willy.interviewdemo2.data.repository

import com.google.gson.Gson
import com.willy.interviewdemo2.data.api.model.Drama
import com.willy.interviewdemo2.data.api.service.TvService
import com.willy.interviewdemo2.utils.Log
import io.reactivex.Single

class TvRepository(private val service: TvService) {

    /**
     * 取得 Drama 資訊
     */
    fun getDramaInfo(): Single<ArrayList<Drama>> {
        Log.d("Start API: getDramaInfo")
        return service.getInterviewDramas()
            .flatMap { rs ->

                Log.d("End API: getDramaInfo")
                Log.json(Gson().toJson(rs))

                // 轉換後回傳
                Single.just(rs.dramas)
            }
    }
}