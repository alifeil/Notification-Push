package com.alifeil.notificationpushfirebase

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        webviewsetup()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    private fun webviewsetup(){

        webview.webViewClient = WebViewClient()
        webview.apply {

            loadUrl("https://www.google.com/")
            settings.javaScriptEnabled = true
            settings.safeBrowsingEnabled = true


        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if(webview.canGoBack()) webview.goBack() else super.onBackPressed()
    }
}