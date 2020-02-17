package com.willy.interviewdemo2.ui.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.flexbox.*
import com.willy.interviewdemo2.R
import com.willy.interviewdemo2.base.BaseFragment
import com.willy.interviewdemo2.data.api.model.Drama
import com.willy.interviewdemo2.extension.hideKeyboard
import com.willy.interviewdemo2.utils.SettingPrefs
import kotlinx.android.synthetic.main.first_fragment.*


class FirstFragment : BaseFragment() {

    private lateinit var viewModel: FirstViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.first_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FirstViewModel::class.java)

        // 訂閱
        observe()
        // 監聽
        viewListener()
        // View 初始設定
        initView()
        // 載入初始資料
        intiData()
    }

    private fun viewListener() {
        // 打字時搜尋資料
        searchEdit.addTextChangedListener {
            viewModel.keywordSearchDramas(it.toString())
            settingPrefs.setString(SettingPrefs.DB_KEY_LAST_SEARCH, it.toString())
        }

        // 失去焦點時儲存 keyword
        searchEdit.setOnFocusChangeListener { v, b ->
            if (!b) {
                val keyword = (v as AppCompatEditText).text.toString()
                viewModel.recordTag(keyword)
            }
        }

        // 清除關鍵字藏鍵盤
        clearIcon.setOnClickListener {
            searchEdit.clearFocus()
            searchEdit.setText("")
            settingPrefs.setString(SettingPrefs.DB_KEY_LAST_SEARCH, "")
            requireContext().hideKeyboard(it)
        }

        // click tag 熱門
        popularTag.setOnClickListener { viewModel.getPopularDramas() }
        // click tag 最新
        newestTag.setOnClickListener { viewModel.getNewestDramas() }
        // click tag 評分最高
        ratingTag.setOnClickListener { viewModel.getRatingDramas() }
    }

    private fun intiData() {
        viewModel.getHotTags()
        viewModel.getRecentTags()
    }

    private fun initView() {
        // 讀取最後搜索記錄
        val lastKeyword = settingPrefs.getString(SettingPrefs.DB_KEY_LAST_SEARCH)
        searchEdit.setText(lastKeyword)

        // Drama Layout Manager
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Hot Tags
        val hotFlexboxManager =
            FlexboxLayoutManager(requireContext(), FlexDirection.ROW, FlexWrap.WRAP)
        hotFlexboxManager.alignItems = AlignItems.FLEX_START
        hotFlexboxManager.justifyContent = JustifyContent.FLEX_START
        hotRecyclerView.layoutManager = hotFlexboxManager

        // Recent Tags
        val recentFlexboxManager =
            FlexboxLayoutManager(requireContext(), FlexDirection.ROW, FlexWrap.WRAP)
        recentFlexboxManager.alignItems = AlignItems.FLEX_START
        recentFlexboxManager.justifyContent = JustifyContent.FLEX_START
        recentRecyclerView.layoutManager = recentFlexboxManager
    }

    private fun observe() {
        // Set Dramas
        viewModel.showDramasLiveData.observe(
            viewLifecycleOwner,
            Observer {
                processResponse(it) { data ->
                    recyclerView.adapter = DramaAdapter(requireContext(), data, dramaClick)
                }
            }
        )

        // Set Hot Tags
        viewModel.hotTagsLiveData.observe(
            viewLifecycleOwner,
            Observer {
                processResponse(it) { data ->
                    hotRecyclerView.adapter = TagAdapter(requireContext(), data, tagClick)
                }
            }
        )

        // Set Recent Tags
        viewModel.recentTagsLiveData.observe(
            viewLifecycleOwner,
            Observer {
                processResponse(it) { data ->
                    recentRecyclerView.adapter = TagAdapter(requireContext(), data, tagClick)
                }
            }
        )

        // show msg
        viewModel.showSnackbarLiveData.observe(
            viewLifecycleOwner,
            Observer<String> { if (it != null) showSnackbarLong(it) })
    }

    // Click Action For RecyclerView
    private val dramaClick = { drama: Drama ->
        // 帶資料轉頁
        switchFragment(FirstFragmentDirections.actionFirstFragmentToSecondFragment(drama))
    }

    // Click Action For Tags
    private val tagClick = { keyword: String ->
        searchEdit.setText(keyword)
        viewModel.recordTag(keyword)
    }
}
