package im.vector.app.eachchat.push.vivo

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.facebook.stetho.common.LogUtil
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import com.vivo.push.PushClient
import im.vector.app.R
import im.vector.app.eachchat.base.BaseModule
import im.vector.app.eachchat.push.AbsPush
import im.vector.app.eachchat.push.PushHelper
import im.vector.app.eachchat.push.PushHelper.Companion.getInstance
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers

/**
 * Created by zhouguanjie on 2021/4/19.
// */
//class VivoPush(context: Context) : AbsPush(context) {
//    var regID: String? = null
//    var pns: String? = null
//
//    @SuppressLint("CheckResult")
//    override fun init(context: Context) {
//        LogUtil.i("## vivo init")
//        pns = "vivo"
//        PushClient.getInstance(context).initialize();
//
////        // 打开push开关, 关闭为turnOffPush，详见api接入文档
////        Observable.create<Any?> { emitter: ObservableEmitter<Any?> ->
////            var result = true
////            try {
////                // 打开push开关, 关闭为turnOffPush，详见api接入文档
////                PushClient.getInstance(context).turnOnPush {
////                    regID = PushClient.getInstance(context).getRegId()
////                    // TODO: 开关状态处理， 0代表成功
////                    if (it == 0) {
////                        regID = PushClient.getInstance(context).getRegId()
////                        pns = "huawei"
////                        getInstance().bindDevice(regId)
////                    } else {
////                        LogUtil.i("## vivo init,turnOnPush not ok")
////                    }
////
////                }
////            } catch (e: ApiException) {
////                e.printStackTrace()
////                result = false
////            }
////            emitter.onNext(result)
////        }.subscribeOn(Schedulers.newThread())
////                .subscribe {
////                    LogUtil.i(
////                            "## vivo init appId = " + context.getString(R.string.vivo_appid)
////                    )
////                }
//    }
//
//    override fun startPush() {
//        LogUtil.i("## vivo startPush")
//        PushClient.getInstance(BaseModule.getContext()).turnOnPush {
//            if (it == 0) {
//                regID = PushClient.getInstance(BaseModule.getContext()).getRegId()
//                LogUtil.i("## vivo startPush,turnOnPush ok")
//            }
//        }
//    }
//
//    override fun stopPush() {
//        LogUtil.i("## vivo stopPush")
//        PushClient.getInstance(BaseModule.getContext()).turnOffPush(null)
//    }
//
//    override fun getRegId(): String? {
//        LogUtil.i("## vivo getRegId = ${PushClient.getInstance(BaseModule.getContext()).regId}")
//        return PushClient.getInstance(BaseModule.getContext()).regId
//    }
//
//    override fun getPNS(): String? {
//        return pns
//    }
//
//    override fun setBadgeCount(context: Context?, count: Int) {
//    }
//
//    override fun clearPush() {
//        LogUtil.i("## vivo clearNotifications")
//        val notificationManager = BaseModule.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.cancelAll()
//    }
//}
