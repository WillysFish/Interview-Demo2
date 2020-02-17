package com.willy.interviewdemo2.ui.first

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.willy.interviewdemo2.R
import com.willy.interviewdemo2.data.api.model.Drama
import com.willy.interviewdemo2.extension.from_yyyyMMddHHmmss
import com.willy.interviewdemo2.extension.inflate
import com.willy.interviewdemo2.extension.yyyyMMdd
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_info.*
import kotlinx.android.synthetic.main.item_top.*
import java.util.*

class DramaAdapter(
    private val mContext: Context,
    private val mData: ArrayList<Drama>,
    private val listener: (drama: Drama) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {

    /**
     * item 的 種類
     */
    companion object {
        private const val TYPE_INFO = 0
        private const val TYPE_TOP = 1
        private const val TYPE_NOT_FOUND = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            TYPE_NOT_FOUND ->
                NotFoundVH(mContext.inflate(R.layout.item_not_found, parent, false))
            TYPE_TOP ->
                TopVH(mContext.inflate(R.layout.item_top, parent, false))
            else ->
                InfoVH(mContext.inflate(R.layout.item_info, parent, false))
        }


    // 增加 not found 時的畫面
    override fun getItemCount(): Int = mData.size.let { if (it == 0) 1 else it }

    /**
     * 綁定 View and Data
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // not found 沒有 data
        if (holder.itemViewType == TYPE_NOT_FOUND) return

        val data = mData[position]

        if (holder.itemViewType == TYPE_TOP) {
            val topVh = holder as TopVH

            // 我的 Blog 文章： Glide Green Image: http://0rz.tw/CvirR
            Glide.with(mContext)
                .load(data.thumb)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.warning_sign)
                .error(R.drawable.warning_sign)
                .fitCenter()
                .into(topVh.itemTopThumb)

            topVh.itemTopName.text = data.name
            topVh.itemTopDate.text = Date().from_yyyyMMddHHmmss(data.createdAt).yyyyMMdd
            topVh.itemTopRating.rating = data.rating

            // 我的 Blog 文章： OnClick: http://0rz.tw/tPoCT
            topVh.itemTopCardView.setOnClickListener { listener(data) }
        } else {
            val infoVh = holder as InfoVH

            var thumbUrl = data.thumb
            var nameStr = data.name
            // 故意弄壞圖片, 名字加長 show UI
            if (position == 3) {
                thumbUrl = data.thumb + "999"
                nameStr += "123456789"
            }

            Glide.with(mContext)
                .load(thumbUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.warning_sign)
                .error(R.drawable.warning_sign)
                .fitCenter()
                .into(infoVh.itemImgThumb)

            infoVh.itemImgName.text = nameStr
            infoVh.itemImgDate.text = Date().from_yyyyMMddHHmmss(data.createdAt).yyyyMMdd
            infoVh.itemImgRating.rating = data.rating

            infoVh.itemImgCardView.setOnClickListener { listener(data) }
        }
    }

    /**
     * 依情況給予 view type
     * 1、無資料
     * 2、頂端 view
     */
    override fun getItemViewType(position: Int): Int {
        if (mData.size == 0) return TYPE_NOT_FOUND
        if (position == 0) return TYPE_TOP
        return TYPE_INFO
    }

    /**
     * 設定 GridLayoutManager
     * top & not found 佔整列空間
     * 其餘一列放 2 個 view
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (getItemViewType(position)) {
                        TYPE_TOP -> 2
                        TYPE_NOT_FOUND -> 2
                        else -> 1
                    }
                }
            }
        }
    }


    inner class TopVH(override val containerView: View) :
        ViewHolder(containerView), LayoutContainer

    inner class InfoVH(override val containerView: View) :
        ViewHolder(containerView), LayoutContainer

    inner class NotFoundVH(override val containerView: View) :
        ViewHolder(containerView), LayoutContainer
}
