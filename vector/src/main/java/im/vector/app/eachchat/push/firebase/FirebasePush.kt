package ai.workly.eachchat.android.push.firebase

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging
import im.vector.app.eachchat.base.BaseModule
import im.vector.app.eachchat.push.AbsPush
import im.vector.app.eachchat.push.PushHelper.Companion.getInstance
import timber.log.Timber

/**
 * Created by zhouguanjie on 2020/12/24.
 */
class FirebasePush(context: Context?) : AbsPush(context) {

    var token : String? = null
    var pns :String?=null
    @RequiresApi(Build.VERSION_CODES.N)
    override fun init(context: Context) {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseAnalytics.getInstance(context).setAnalyticsCollectionEnabled(true)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.e("push", "Fetching FCM registration token failed")
//                PushUtils.downgrading()
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result
            pns="fcm"
            getInstance().bindDevice(token)
            Timber.i("push", "Fetching FCM registration token success token = $token")
        })
    }

    override fun startPush() {

    }

    override fun stopPush() {
    }

    override fun getRegId(): String? {
//        val token = FirebaseMessaging.getInstance().token
//        return token.result
        return token
    }


    override fun getPNS(): String? {
//        val token = FirebaseMessaging.getInstance().token
//        return token.result
        return pns
    }

    override fun setBadgeCount(context: Context?, count: Int) {
    }

    override fun clearPush() {
        val notificationManager = BaseModule.getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }
}
