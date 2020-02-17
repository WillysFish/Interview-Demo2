package com.willy.interviewdemo2.ui.first

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.willy.interviewdemo2.base.BaseViewModel
import com.willy.interviewdemo2.data.DataResponse
import com.willy.interviewdemo2.data.ErrorCode
import com.willy.interviewdemo2.data.api.model.Drama
import com.willy.interviewdemo2.data.db.DbRepoFactory
import com.willy.interviewdemo2.data.repository.TvDbRepository.OrderBy
import com.willy.interviewdemo2.utils.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirstViewModel(application: Application) : BaseViewModel(application) {
    private val dbRepo = DbRepoFactory.getDramaRepo(getApplication())
    var showDramasLiveData = MutableLiveData<DataResponse<ArrayList<Drama>>>()
    var hotTagsLiveData = MutableLiveData<DataResponse<ArrayList<String>>>()
    var recentTagsLiveData = MutableLiveData<DataResponse<ArrayList<String>>>()

    fun getPopularDramas() = getDramas(OrderBy.VIEWS)
    fun getNewestDramas() = getDramas(OrderBy.CREATE_AT)
    fun getRatingDramas() = getDramas(OrderBy.RATING)

    /**
     * 取得戲劇列表
     */
    private fun getDramas(orderBy: OrderBy = OrderBy.VIEWS) {
        disposables.add(
            dbRepo.getDramasByOrder(orderBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showDramasLiveData.value = DataResponse(it)
                }, {
                    showDramasLiveData.value = DataResponse(ErrorCode.findErrorByThrowable(it))
                    Log.e(it, "getPopularDramas() Error")
                })
        )
    }

    /**
     * 以關鍵字搜尋 Dramas
     */
    fun keywordSearchDramas(keyword: String) {
        disposables.add(
            dbRepo.getDramasByKeyword(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showDramasLiveData.value = DataResponse(it)
                }, {
                    showDramasLiveData.value = DataResponse(ErrorCode.findErrorByThrowable(it))
                    Log.e(it, "keywordSearchDb() Error")
                })
        )
    }

    fun recordTag(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepo.addTag(keyword)
        }
    }

    /**
     * 取得搜尋次數最高前三名
     */
    fun getHotTags() {
        disposables.add(
            dbRepo.getTagByOrder(OrderBy.TIMES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    hotTagsLiveData.value = DataResponse(it)
                }) {
                    Log.e(it, "getHotTags")
                }
        )
    }

    /**
     * 取得最近前 10 次搜尋關鍵字
     */
    fun getRecentTags() {
        disposables.add(
            dbRepo.getTagByOrder(OrderBy.UPDATE_AT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    recentTagsLiveData.value = DataResponse(it)
                }) {
                    Log.e(it, "getRecentTags")
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
