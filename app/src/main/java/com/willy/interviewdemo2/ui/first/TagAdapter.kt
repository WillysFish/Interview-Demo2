package com.willy.interviewdemo2.ui.first

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.flexbox.FlexboxLayoutManager.LayoutParams
import com.willy.interviewdemo2.R
import com.willy.interviewdemo2.extension.dpToPx

class TagAdapter(
    private val mContext: Context,
    private val mData: ArrayList<String>,
    private val listener: (keyword: String) -> Unit
) : RecyclerView.Adapter<TagAdapter.TagVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagVH {
        val view = AppCompatTextView(mContext)
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(
            mContext.dpToPx(4),
            mContext.dpToPx(4),
            mContext.dpToPx(4),
            mContext.dpToPx(4)
        )
        view.layoutParams = params
        view.background = mContext.getDrawable(R.drawable.tag_bg_corners)
        view.maxLines = 1
        view.setPadding(
            mContext.dpToPx(16),
            mContext.dpToPx(4),
            mContext.dpToPx(16),
            mContext.dpToPx(4)
        )
        return TagVH(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: TagVH, position: Int) {
        val data = mData[position]
        val view = (holder.itemView as AppCompatTextView)

        view.text = data
        view.setOnClickListener { listener(data) }
    }

    inner class TagVH(view: AppCompatTextView) : ViewHolder(view)
}