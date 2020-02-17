package com.willy.interviewdemo2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.willy.interviewdemo2.R
import com.willy.interviewdemo2.extension.from_yyyyMMddHHmmss
import com.willy.interviewdemo2.extension.yyyyMMdd
import kotlinx.android.synthetic.main.second_fragment.*
import java.text.NumberFormat
import java.util.*


class SecondFragment : Fragment() {

    private val args: SecondFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.second_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val drama = args.drama
        val viewStr = NumberFormat.getInstance().format(drama.totalViews)

        secondName.text = drama.name
        secondDate.text = Date().from_yyyyMMddHHmmss(drama.createdAt).yyyyMMdd
        secondViews.text = getString(R.string.show_views).format(viewStr)
        secondRating.rating = drama.rating

        Glide.with(requireContext())
            .load(drama.thumb)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .placeholder(R.drawable.warning_sign)
            .error(R.drawable.warning_sign)
            .fitCenter()
            .into(secondImg)
    }

}
