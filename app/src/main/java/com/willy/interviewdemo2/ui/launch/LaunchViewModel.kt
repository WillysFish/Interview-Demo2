package com.willy.interviewdemo2.ui.launch

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.willy.interviewdemo2.base.BaseViewModel
import com.willy.interviewdemo2.data.DataResponse
import com.willy.interviewdemo2.data.ErrorCode
import com.willy.interviewdemo2.data.api.ApiRepoFactory
import com.willy.interviewdemo2.data.db.DbRepoFactory
import com.willy.interviewdemo2.utils.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// 我的 Blog 文章： MVC、MVP、MVVM: http://0rz.tw/MJqbg
class LaunchViewModel(application: Application) : BaseViewModel(application) {
    private val dbRepo = DbRepoFactory.getDramaRepo(getApplication())
    var updateDbLiveData = MutableLiveData<DataResponse<Boolean>>()

    /**
     * 更新 DB 資料至最新
     */
    fun updateDb() {
        disposables.add(
            ApiRepoFactory.tvRepository.getDramaInfo()
                .subscribeOn(Schedulers.io())
                .doOnSuccess { list ->
                    dbRepo.addDramas(list)
                }.observeOn(
                    AndroidSchedulers.mainThread()
                ).subscribe({
                    updateDbLiveData.value = DataResponse(true)
                }, {
                    Log.e(it, "updateDb()")
                    updateDbLiveData.value = DataResponse(ErrorCode.DB_NOT_NEWEST)
                })
        )
    }

    /**
     * 預先設定一些 Tag 模擬後台的搜尋記錄
     */
    fun addTagsForDemo() {
        disposables.add(
            Observable.fromArray(
                "我是被搜尋的關鍵字第一名",
                "我是被搜尋的關鍵字第一名",
                "我是被搜尋的關鍵字第一名",
                "我是被搜尋的關鍵字第一名",
                "搜尋第二名",
                "搜尋第二名",
                "搜尋第二名",
                "被搜尋次數第三名",
                "被搜尋次數第三名",
                "獅子王",
                "101",
                "Line",
                "周子瑜",
                "事物所",
                "事務所",
                "冰與火之歌",
                "獵魔士"
            ).subscribeOn(
                Schedulers.io()
            ).subscribe({
                dbRepo.addTag(it)
            }) {
                Log.e(it, "addTagsForDemo")
            }
        )
    }
}
