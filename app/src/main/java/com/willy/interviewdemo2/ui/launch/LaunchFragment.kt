package com.willy.interviewdemo2.ui.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.willy.interviewdemo2.R
import com.willy.interviewdemo2.base.BaseFragment
import com.willy.interviewdemo2.data.ErrorCode
import com.willy.interviewdemo2.ui.MainActivity
import com.willy.interviewdemo2.utils.SettingPrefs


class LaunchFragment : BaseFragment() {

    private lateinit var viewModel: LaunchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_launch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LaunchViewModel::class.java)

        // 首開啟 app，初始化 模擬DB
        if (!settingPrefs.getBoolean(SettingPrefs.DB_KEY_LAUNCHED)) {
            viewModel.addTagsForDemo()
            settingPrefs.setBoolean(SettingPrefs.DB_KEY_LAUNCHED, true)
        }

        // 更新資料給 DB
        viewModel.updateDb()

        // 訂閱
        observe()
    }

    // 我的 Blog 文章： Memory Leak of LiveData: http://0rz.tw/HmSWx
    private fun observe() {
        // 前置作業完成，跳頁
        viewModel.updateDbLiveData.observe(
            viewLifecycleOwner,
            Observer {
                val data = it.rsData
                val error = it.error
                if (!(error == ErrorCode.NO_ERROR && data != null)) {
                    val activity = (requireContext() as MainActivity)
                    activity.showActionSnackbar(getString(error.memo)) { activity.openSetting() }
                }
                switchFragment(LaunchFragmentDirections.actionLaunchFragmentToFirstFragment())
            }
        )
    }
}
