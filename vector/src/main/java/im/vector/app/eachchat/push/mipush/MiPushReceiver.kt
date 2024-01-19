/*
 * Copyright (c) 2022 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package im.vector.app.eachchat.push.mipush

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.facebook.stetho.common.LogUtil
import com.xiaomi.mipush.sdk.ErrorCode
import im.vector.app.eachchat.push.PushHelper.Companion.getInstance
import com.xiaomi.mipush.sdk.PushMessageReceiver
import com.xiaomi.mipush.sdk.MiPushMessage
import com.xiaomi.mipush.sdk.MiPushCommandMessage
import com.xiaomi.mipush.sdk.MiPushClient
import kotlinx.coroutines.DelicateCoroutinesApi


/**
 * Created by zhouguanjie on 2020/1/10.
 */
class MiPushReceiver : PushMessageReceiver() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onNotificationMessageArrived(context: Context, miPushMessage: MiPushMessage) {
        super.onNotificationMessageArrived(context, miPushMessage)
        // 用来接收服务器发来的通知栏消息（消息到达客户端时触发，并且可以接收应用在前台时不弹出通知的通知消息）
//        PushHelper.getInstance().syncMessage(context);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            try {
//                GlobalScope.launch(Dispatchers.IO) {
//                    val roomSummaries = withContext(Dispatchers.IO) {
//                        val membershipList: MutableList<Membership> = ArrayList()
//                        membershipList.add(Membership.JOIN)
//                        val params = RoomSummaryQueryParams.Builder().apply {
//                            memberships = membershipList
//                        }.build()
//                        BaseModule.getSession().getRoomSummaries(params)
//                    }
//                    if (roomSummaries.isEmpty()) {
//                        YQBadgeUtils.setCount(0, context)
//                    }
//                    var count = 0
//                    roomSummaries.forEach {
//                        count += it.notificationCount
//                    }
//                    val notification =
//                            Notification.Builder(context, "LISTEN_FOR_EVENTS_NOTIFICATION_CHANNEL_ID")
//                                    .setSmallIcon(R.drawable.ic_launcher)
//                                    .setContentTitle(context.getString(R.string.yiqia))
//                                    .setContentText(context.getString(R.string.you_have_n_unread_message, count.toString()))
//                                    .setNumber(count)
//                                    .setChannelId("LISTEN_FOR_EVENTS_NOTIFICATION_CHANNEL_ID")
//                                    .build()
//                    val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                    ShortcutBadger.applyNotification(BaseModule.getContext(), notification, count)
//                    mNotificationManager.notify(999, notification)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        getInstance().setBadge(context)
        LogUtil.i("## mi onNotificationMessageArrived")
    }

    override fun onNotificationMessageClicked(context: Context, miPushMessage: MiPushMessage) {
        super.onNotificationMessageClicked(context, miPushMessage)
        // 用来接收服务器发来的通知栏消息（用户点击通知栏时触发）
        getInstance().clickNotification(context)
        LogUtil.i("## mi onNotificationMessageClicked")
    }

    override fun onCommandResult(context: Context, miPushCommandMessage: MiPushCommandMessage) {
        super.onCommandResult(context, miPushCommandMessage)
        // 用来接收客户端向服务器发送命令消息后返回的响应
        LogUtil.i("## mi onCommandResult")
    }

    override fun onReceiveRegisterResult(context: Context, message: MiPushCommandMessage) {
        super.onReceiveRegisterResult(context, message)
        // 用来接受客户端向服务器发送注册命令消息后返回的响应
        LogUtil.i("## mi onReceiveRegisterResult message = $message")
        val command = message.command
        val arguments = message.commandArguments
        val cmdArg1 = if (arguments != null && arguments.size > 0) arguments[0] else null
        // val cmdArg2 = if (arguments != null && arguments.size > 1) arguments[1] else null
        if (MiPushClient.COMMAND_REGISTER == command) {
            if (message.resultCode == ErrorCode.SUCCESS.toLong()) {
                getInstance().bindDevice(cmdArg1)
            }
        }
    }
}
