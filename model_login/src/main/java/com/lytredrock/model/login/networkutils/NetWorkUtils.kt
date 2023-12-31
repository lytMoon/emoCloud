package com.lytredrock.model.login.networkutils

import android.annotation.SuppressLint
import android.util.Log
import com.lytredrock.model.login.apiservice.ApiService
import com.lytredrock.model.login.apiservice.IVerifyCodeInfo
import com.lytredrock.model.login.apiservice.MusicInfoCallBack
import com.lytredrock.model.login.apiservice.qrCallBack
import com.lytredrock.model.login.logindata.CodeData
import com.lytredrock.model.login.logindata.CodeNum
import com.lytredrock.model.login.logindata.Comment
import com.lytredrock.model.login.logindata.MusicComment
import com.lytredrock.model.login.logindata.QRData
import com.lytredrock.model.login.logindata.QRKey
import com.lytredrock.model.login.logindata.QRLast
import com.lytredrock.model.login.logindata.QRPic
import com.lytredrock.model.login.logindata.QRPicData
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers.*
import io.reactivex.disposables.Disposable

import io.reactivex.schedulers.Schedulers
import com.lytredrock.model.login.apiservice.PhoneNumCallBack as PhoneNumCallBack1

/**
 * description ：把网络请求的具体逻辑封装成一个单例类
 * author : lytMoon
 * email : yytds@foxmail.com
 * date : 2023/7/14 17:28
 * version: 1.0
 */
object NetWorkUtils {
    var receivedNumber: String? = null
    var qrUrl: String? = null
    private val apiService = ServiceCreatorUtils.create(ApiService::class.java)


    /**
     * 发送请求，得到二维码的key值
     */
    fun ReceiveQRKey() {
        apiService.qrKeyGet()
            .subscribeOn(Schedulers.newThread())//新开一个线程进行请求
            .observeOn(mainThread())//在安卓主线程（执行onNext的逻辑）
            .subscribe(object : Observer<QRKey<QRData>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.d("ReceiveQRKey", "(BaseViewModel.kt:82)-->> ${e.message}");
                }

                override fun onComplete() {
                }

                override fun onNext(t: QRKey<QRData>) {

                    receivedNumber = t.data.unikey
                    Log.d("ReceiveQRKey", "(BaseViewModel.kt:90)-->> $receivedNumber")
                }

            })

    }

    /**
     * 得到图片相关信息
     */

    fun receiveQRPic(key: String, callBack: MusicInfoCallBack) {
        apiService.qrCreat(key)
            .subscribeOn(Schedulers.newThread())//新开一个线程进行请求
            .observeOn(mainThread())//在安卓主线程（执行onNext的逻辑）
            .subscribe(object : Observer<QRPic<QRPicData>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    callBack.onFailed(e.message.toString())
                }

                override fun onComplete() {

                }

                override fun onNext(t: QRPic<QRPicData>) {
                    callBack.onRespond(t.data.qrimg)
                    Log.d("receiveQRPic", "(NetWorkUtils.kt:125)-->> " + t.data.qrimg);

                }

            })

    }

    /**
     * 验证二维码扫码状态
     */
    fun receiveQRState(key: String, callBack: qrCallBack) {
        apiService.qrLogin(key)
            .subscribeOn(Schedulers.newThread())//新开一个线程进行请求
            .observeOn(mainThread())//在安卓主线程（执行onNext的逻辑）
            .subscribe(object : Observer<QRLast> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    callBack.onFailed(e.message.toString())
                }

                override fun onComplete() {

                }

                override fun onNext(t: QRLast) {
                    when (t.code) {
                        800 -> callBack.onFailed("二维码过期")
                        801 -> callBack.onFailed("等待扫码")
                        802 -> callBack.onFailed("待确认")
                        803 -> callBack.onRespond(t)
                    }

                }

            })

    }


    /**
     * 获得手机验证码
     */

    fun receiveCodeNum(phoneNum: String, callBack: PhoneNumCallBack1) {
        apiService.codeSend(phoneNum)
            .subscribeOn(Schedulers.newThread())//新开一个线程进行请求
            .observeOn(mainThread())//在安卓主线程（执行onNext的逻辑）
            .subscribe(object : Observer<CodeNum> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    callBack.onFailed(e.message.toString())
                    Log.d("receiveCodeNum", "(NetWorkUtils.kt:151)-->>${e.message} ");
                }

                override fun onComplete() {

                }

                override fun onNext(t: CodeNum) {
                    callBack.onRespond(t)
                    Log.d("receiveCodeNum", "(NetWorkUtils.kt:160)-->> " + t.data + t.data);
                }

            })
    }

    /**
     * 验证验证码
     */
    fun receiveCodeState(phone: String, captcha: String, callBack: IVerifyCodeInfo) {

        apiService.codeIdentify(phone, captcha)
            .subscribeOn(Schedulers.newThread())//新开一个线程进行请求
            .observeOn(mainThread())//在安卓主线程（执行onNext的逻辑）
            .subscribe(object : Observer<CodeData> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    callBack.onFailed(e.message.toString())
                }

                override fun onComplete() {
                }

                override fun onNext(t: CodeData) {
                    Log.d(
                        "receiveCodeState",
                        "(NetWorkUtils.kt:184)-->> ${t.code},${t.data},${t.message}"
                    );
                    callBack.onRespond(t.data)//这里的data是true或者false类型的
                }


            })


    }


}