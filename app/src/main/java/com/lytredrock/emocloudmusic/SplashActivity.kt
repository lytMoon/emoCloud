package com.lytredrock.emocloudmusic

import BaseActivity
import android.annotation.SuppressLint
import android.os.Bundle
import com.bumptech.glide.Glide
import com.lytredrock.emocloudmusic.databinding.ActivitySplashBinding
import java.util.Timer
import java.util.TimerTask

class SplashActivity : BaseActivity() {
    private val timer = Timer()
    private val myViewBinding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(
            layoutInflater
        )
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(myViewBinding.root)
        transparentStatusBar(window, false)
        var boolean = true

//        设置一个跳转方法
        myViewBinding.tvSkip.setOnClickListener {
            startActivity<MainActivity>()
            boolean = false
            finish()
        }

        Glide.with(this).load(R.drawable.splash).into(myViewBinding.ivSplash)

//设置了一个定时器，在4秒后会跳转页面
        timer.schedule(object : TimerTask() {
            @SuppressLint("SuspiciousIndentation")
            override fun run() {
                if (boolean)
                    startActivity<MainActivity>()
                finish()
            }
        }, 4000)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}