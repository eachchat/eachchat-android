package im.vector.app.eachchat.push.vivo;
//
//import android.content.Context
//import com.facebook.stetho.common.LogUtil
//import com.vivo.push.model.UPSNotificationMessage
//import com.vivo.push.model.UnvarnishedMessage
//import com.vivo.push.sdk.BasePushMessageReceiver
//import im.vector.app.eachchat.base.BaseModule
//import im.vector.app.eachchat.utils.YQBadgeUtils
//import org.matrix.android.sdk.api.session.room.model.Membership
//import org.matrix.android.sdk.api.session.room.roomSummaryQueryParams
//
///**
// * Created by zhouguanjie on 2021/4/19.
// */
//class PushMessageReceiverImpl : BasePushMessageReceiver() {
//    override fun onNotificationMessageClicked(p0: Context?, p1: UPSNotificationMessage?) {
//    }
//
//    override fun onNotificationMessageArrived(p0: Context?, p1: UPSNotificationMessage?): Boolean {
//        LogUtil.i("##vivo接收通知")
//        kotlin.runCatching {
////            val queryParams = roomSummaryQueryParams {
////                memberships = listOf(Membership.JOIN)
////            }
//            // val roomSummaries = BaseModule.getSession().getRoomSummaries(queryParams)
//            // val unreadRoomSummaries = roomSummaries.filter { it.hasUnreadMessages }
//            // YQBadgeUtils.setCount(unreadRoomSummaries.size, BaseModule.getContext())
//        }.exceptionOrNull()?.let{
//            return false
//        }
//        return false
//    }
//
//    override fun onReceiveRegId(p0: Context?, p1: String?) {
//    }
//}
