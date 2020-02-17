package com.willy.interviewdemo2.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.willy.interviewdemo2.R
import com.willy.interviewdemo2.utils.AESUtil


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init
        AESUtil.init(this)
    }

    /**
     * 跨 Fragment SnackBar
     */
    fun showActionSnackbar(msg: String, action: (view: View) -> Unit) =
        Snackbar.make(findViewById(R.id.container), msg, Snackbar.LENGTH_INDEFINITE).setAction(
            "檢查網路",
            action
        ).show()

    /**
     * 開啟設定頁
     */
    fun openSetting() {
        val intent = Intent()
        intent.action = Settings.ACTION_SETTINGS
        startActivity(intent)
    }
}
