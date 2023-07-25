package com.lytredrock.model.player.fragment

import RvCommentAdapter
import android.annotation.SuppressLint

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lytredrock.lib.base.BaseUtils.myToast
import com.lytredrock.model.player.R
import com.lytredrock.model.player.databinding.FragmentPageOneBinding
import com.lytredrock.model.player.viewmodel.MusicPlayerViewModel

class PageOneFragment : Fragment() {


    //懒加载注入viewModel
    private val playerViewModel by lazy {
        ViewModelProvider(requireActivity())[MusicPlayerViewModel::class.java]
    }

    //懒加载注入viewBinding
    private val mBinding: FragmentPageOneBinding by lazy {
        FragmentPageOneBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniMusicInfo()
        iniAnimation()
        iniClick()
    }

    @SuppressLint("CheckResult", "Range")
    private fun iniClick() {
        mBinding.ivComments.setOnClickListener {
            iniComments()
        }
        mBinding.ivDownload.setOnClickListener {
            myToast("下载",requireContext())

        }
    }




    /**
     * 更新评论区
     */
    private fun iniComments() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(bottomSheetView)
        val recyclerView: RecyclerView = bottomSheetDialog.findViewById(R.id.rv_comment)!!
        playerViewModel.musicCommentsData.observe(requireActivity()) {
            try {
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = RvCommentAdapter(it, requireActivity())
            } catch (e: java.lang.NullPointerException) {
                Log.d("NullPointerException", "空指针异常");
            }
        }
        /**设置 BottomSheetDialog 的一些参数
         */
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setCanceledOnTouchOutside(true)
        bottomSheetDialog.behavior.isDraggable = true
        bottomSheetDialog.behavior.peekHeight = 1500
        bottomSheetDialog.show()


    }


    /**
     * 初始化动画
     */
    private fun iniAnimation() {
        /**
         * 下面几个参数分别表示：从多少度旋转，旋转到多少度，x轴旋转中心，y轴旋转中心，持续时间（旋转周期），无限次数
         */
        val animation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        animation.duration = 10000
        animation.repeatCount = Animation.INFINITE
        animation.interpolator = LinearInterpolator() // 使用 LinearInterpolator 插值器，保持播放速度一样
        mBinding.ivMusicCover.startAnimation(animation)
    }


    /**
     * 更新音乐的相关信息，ui相关
     */
    private fun iniMusicInfo() {
        playerViewModel.musicInfo.observe(viewLifecycleOwner) {
            Glide.with(this)
                .load(it[0].al.picUrl)
                .transform(RoundedCorners(360))
                .into(mBinding.ivMusicCover)
        }
    }


}