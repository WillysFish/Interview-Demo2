package com.willy.interviewdemo2.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val disposables = CompositeDisposable()
    val showSnackbarLiveData = MutableLiveData<String>()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}