/*
 * Copyright (c) 2023 New Vector Ltd
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
package im.vector.app.push.getui

//import android.content.Context
//import com.igexin.sdk.GTIntentService
//import com.igexin.sdk.message.GTCmdMessage
//import com.igexin.sdk.message.GTNotificationMessage
//import com.igexin.sdk.message.GTTransmitMessage
//import timber.log.Timber
//
//class IntentService : GTIntentService() {
//    override fun onReceiveServicePid(context: Context, pid: Int) {}
//
//    /**
//     * 此方法用于接收和处理透传消息。透传消息个推只传递数据，不做任何处理，客户端接收到透传消息后需要自己去做后续动作处理，如通知栏展示、弹框等。
//     * 如果开发者在客户端将透传消息创建了通知栏展示，建议将展示和点击回执上报给个推。
//     */
//    override fun onReceiveMessageData(context: Context, msg: GTTransmitMessage) {
//        val payload = msg.payload
//        val data = String(payload)
//        Timber.d(TAG, "receiver payload =--===== $data") //透传消息文本内容
//
//        //taskid和messageid字段，是用于回执上报的必要参数。详情见下方文档“6.2 上报透传消息的展示和点击数据”
////        val taskid = msg.taskId
////        val messageid = msg.messageId
//    }
//
//    // 接收 cid
//    override fun onReceiveClientId(context: Context, clientid: String) {
//        Timber.d(TAG, "onReceiveClientId -> clientid ======= $clientid")
//    }
//
//    // cid 离线上线通知
//    override fun onReceiveOnlineState(context: Context, online: Boolean) {}
//
//    // 各种事件处理回执
//    override fun onReceiveCommandResult(context: Context, cmdMessage: GTCmdMessage) {}
//
//    // 通知到达，只有个推通道下发的通知会回调此方法
//    override fun onNotificationMessageArrived(context: Context, msg: GTNotificationMessage) {}
//
//    // 通知点击，只有个推通道下发的通知会回调此方法
//    override fun onNotificationMessageClicked(context: Context, msg: GTNotificationMessage) {}
//}
