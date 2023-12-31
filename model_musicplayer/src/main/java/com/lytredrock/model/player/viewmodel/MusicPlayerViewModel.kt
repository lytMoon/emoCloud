package com.lytredrock.model.player.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lytredrock.model.player.utils.PlayerNetWorkUtils
import com.lytredrock.model.player.playerData.Data
import com.lytredrock.model.player.playerData.HotComment
import com.lytredrock.model.player.playerData.Lrc
import com.lytredrock.model.player.playerData.MusicProgressData
import com.lytredrock.model.player.playerData.Song

/**
 * description ：
 * author : lytMoon
 * email : yytds@foxmail.com
 * date : 2023/7/19 16:26
 * version: 1.0
 */
class MusicPlayerViewModel : ViewModel() {

    /**
     * 这个livedata直接用于数据更新，不会进行网络请求，只储存音乐播放进度的值，
     * 不private
     */
     val musicProgressData: MutableLiveData<MusicProgressData> = MutableLiveData()


    private val _musicUrlInfo: MutableLiveData<List<Data>> = MutableLiveData()
    val musicUrlInfo: LiveData<List<Data>>
        get() = _musicUrlInfo

    private val _musicInfo: MutableLiveData<List<Song>> = MutableLiveData()
    val musicInfo: LiveData<List<Song>>
        get() = _musicInfo

    private val _musicLyricsInfo: MutableLiveData<List<Lrc>> = MutableLiveData()
    val musicLyricsInfo: LiveData<List<Lrc>>
        get() = _musicLyricsInfo
    //储存歌曲的评论信息
    private val _musicCommentsData: MutableLiveData<List<HotComment>> = MutableLiveData()
    val musicCommentsData: LiveData<List<HotComment>>
        get() = _musicCommentsData


    /**
     * 获取音乐的播放链接
     */
    fun getMusicUrl(key: String) {

        PlayerNetWorkUtils.getUrl(key, _musicUrlInfo)

    }

    /**
     * 传入音乐的id
     */
    fun getMusicInformation(key: String) {
        PlayerNetWorkUtils.receiveMusicInfo(key, _musicInfo)
    }

    /**
     * 获取音乐的歌词
     */

    fun getMusicLyrics(key:String){
        PlayerNetWorkUtils.receiveLyrics(key, _musicLyricsInfo)
    }

    /**
     * 返回得到的评论区的数据
     */
    fun getMusicComments(key: String) {
        PlayerNetWorkUtils.receiveMusicComments(key, _musicCommentsData)

    }
}