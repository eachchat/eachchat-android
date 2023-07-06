package im.vector.app.eachchat.push.vivo

import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import com.facebook.stetho.common.LogUtil
import com.vivo.push.PushClient
import im.vector.app.eachchat.base.BaseModule
import im.vector.app.eachchat.push.AbsPush
import im.vector.app.eachchat.push.PushHelper

/**
 * Created by zhouguanjie on 2021/4/19.
 */
class VivoPush(context: Context) : AbsPush(context) {

    override fun init(context: Context) {
        LogUtil.i("## vivo init")
        PushClient.getInstance(context).initialize();
    }

    override fun startPush() {
        LogUtil.i("## vivo startPush")
        PushClient.getInstance(BaseModule.getContext()).turnOnPush {
            if (it == 0) {
                PushHelper.getInstance().bindDevice(regId)
            }
        }
    }

    override fun stopPush() {
        LogUtil.i("## vivo stopPush")
        PushClient.getInstance(BaseModule.getContext()).turnOffPush(null)
    }

    override fun getRegId(): String? {
        LogUtil.i("## vivo getRegId = ${PushClient.getInstance(BaseModule.getContext()).regId}")
        return PushClient.getInstance(BaseModule.getContext()).regId
    }

    override fun setBadgeCount(context: Context?, count: Int) {

    }

    override fun clearPush() {
        LogUtil.i("## vivo clearNotifications")
        val notificationManager = BaseModule.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }
}
