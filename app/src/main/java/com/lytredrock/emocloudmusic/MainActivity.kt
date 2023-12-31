package com.lytredrock.emocloudmusic

import BaseActivity
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.lytredrock.emocloudmusic.adapter.FragmentAdapter
import com.lytredrock.emocloudmusic.databinding.ActivityMainBinding
import com.lytredrock.emocloudmusic.frgment.BackFragment
import com.lytredrock.emocloudmusic.frgment.FindFragment
import com.lytredrock.emocloudmusic.frgment.HotFragment
import com.lytredrock.emocloudmusic.frgment.MineFragment
import com.lytredrock.model.player.ui.MusicService
import java.util.Timer
import java.util.TimerTask

class MainActivity : BaseActivity() {
    private lateinit var myBinder: MusicService.MusicBinder

    private val timer = Timer()

    private val myConnection = object : ServiceConnection {
        /**
         * activity 和 service 成功绑定的时候会调用
         */
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            //初始化mBinder对象
            myBinder = service as MusicService.MusicBinder
        }

        //service进程创建过程中崩溃或者被杀掉的时候会调用
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("connection", "onServiceDisconnected: ")
        }

    }
    private val myViewBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(myViewBinding.root)
        iniBind()
        transparentStatusBar(window, false)
//        一个图片自旋转的动画
        val rotateAnimation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.duration = 2000
        rotateAnimation.repeatCount = Animation.INFINITE
        rotateAnimation.interpolator = LinearInterpolator()
        myViewBinding.ivFindSong.startAnimation(rotateAnimation)

//设置主页的侧滑栏
        myViewBinding.ivSlideMenu.bringToFront()
        myViewBinding.ivSlideMenu.setOnClickListener {
            myViewBinding.drawerLayout.openDrawer(GravityCompat.START)
        }

        myViewBinding.ivBroadcast.setOnClickListener {
            try {
                if (myBinder.isPlaying()) {
                    myBinder.stop()
                    myViewBinding.ivStop.visibility = View.VISIBLE
                    myViewBinding.ivBroadcast.visibility = View.GONE
                }
            } catch (e: Exception) {
                recreate()
            }

        }
        myViewBinding.ivStop.setOnClickListener {
            try {
                if (!myBinder.isPlaying()) {
                    myBinder.start()
                    myViewBinding.ivStop.visibility = View.GONE
                    myViewBinding.ivBroadcast.visibility = View.VISIBLE
                }
            } catch (e: java.lang.Exception) {
                recreate()
            }

        }
        /**
         * 利用ARouter跳转
         */
        myViewBinding.constraint.setOnClickListener {
            ARouter.getInstance()
                .build("/music/musicPlay")
                .navigation()
        }



        timer.schedule(object : TimerTask() {
            @SuppressLint("SuspiciousIndentation")
            override fun run() {
                runOnUiThread {
                    try {
                        myViewBinding.tvFindSongName.text = myBinder.getMusicName()
                        myViewBinding.tvFindSongAuthor.text = myBinder.getMusicAuthor()
                        if (myBinder.isPlaying()) {
                            myViewBinding.ivFindSong.startAnimation(rotateAnimation)
                        } else {
                            myViewBinding.ivFindSong.clearAnimation()
                        }
                    } catch (e: Exception) {
                        recreate()
                    }

                }
            }
        }, 1000, 1000)


        val fragments = ArrayList<BackFragment>()
        fragments.add(object : BackFragment {
            override fun back(): Fragment {
                return FindFragment()
            }
        })
        fragments.add(object : BackFragment {
            override fun back(): Fragment {
                return MineFragment()
            }
        })
        fragments.add(object : BackFragment {
            override fun back(): Fragment {
                return HotFragment()
            }
        })
        myViewBinding.vp2.adapter = FragmentAdapter(this, fragments)
        myViewBinding.vp2.isUserInputEnabled = false

        myViewBinding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.find -> myViewBinding.vp2.currentItem = 0
                R.id.mine -> myViewBinding.vp2.currentItem = 1
                R.id.community -> myViewBinding.vp2.currentItem = 2
            }
            return@setOnItemSelectedListener true

        }
    }

    /**
     * 绑定service
     */
    private fun iniBind() {
        val intent = Intent(this, MusicService::class.java)
        startService(intent)
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(myConnection)//解除绑定
        timer.cancel()
    }
}


