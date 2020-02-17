package com.willy.interviewdemo2.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.willy.interviewdemo2.data.DataResponse
import com.willy.interviewdemo2.data.ErrorCode
import com.willy.interviewdemo2.utils.Log
import com.willy.interviewdemo2.utils.SettingPrefs

open class BaseFragment : Fragment() {

    lateinit var settingPrefs: SettingPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingPrefs = SettingPrefs.getInstance(requireContext())
    }

    override fun onResume() {
        super.onResume()
        Log.d("[%s] onResume()", javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        Log.d("[%s] onPause()", javaClass.simpleName)
    }

    fun <T> processResponse(response: DataResponse<T>, successBlock: (data: T) -> Unit) {
        val data = response.rsData
        val error = response.error
        if (error == ErrorCode.NO_ERROR && data != null) {
            successBlock(data)
        } else {
            showSnackbarLong(getString(error.memo))
        }
    }

    fun switchFragment(directions: NavDirections) {
        findNavController(requireView()).navigate(directions)
    }

    // 我的 Blog 文章 Snackbar 介紹: http://0rz.tw/dSHMT
    fun showSnackbarLong(msg: String) =
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).show()
}