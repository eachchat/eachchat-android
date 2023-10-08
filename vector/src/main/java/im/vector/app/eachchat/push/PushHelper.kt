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

package im.vector.app.eachchat.push

import ai.workly.eachchat.android.push.firebase.FirebasePush
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import im.vector.app.R
import im.vector.app.core.di.ActiveSessionHolder
import im.vector.app.core.di.DefaultSharedPreferences
import im.vector.app.core.resources.StringProvider
import im.vector.app.eachchat.base.BaseModule
import im.vector.app.eachchat.bean.PNSInput
import im.vector.app.eachchat.net.CloseableCoroutineScope
import im.vector.app.eachchat.net.NetConstant
//import im.vector.app.eachchat.push.getui.GeTuiPush
import im.vector.app.eachchat.push.hwpush.HWPush
import im.vector.app.eachchat.push.mipush.MiPush
import im.vector.app.eachchat.push.oppoPush.OppoPush
import im.vector.app.eachchat.push.vivo.VivoPush
//import im.vector.app.eachchat.push.mipush.MiPush
//import im.vector.app.eachchat.push.oppoPush.OppoPush
//import im.vector.app.eachchat.push.vivo.VivoPush
import im.vector.app.eachchat.service.ApiService
import im.vector.app.eachchat.utils.AppCache
import im.vector.app.eachchat.utils.YQBadgeUtils
import im.vector.app.features.home.HomeActivity
import im.vector.app.features.settings.VectorPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import org.matrix.android.sdk.api.query.ActiveSpaceFilter
import org.matrix.android.sdk.api.query.RoomCategoryFilter
import org.matrix.android.sdk.api.session.pushers.HttpPusher
import org.matrix.android.sdk.api.session.pushers.PushersService
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.roomSummaryQueryParams
import timber.log.Timber
import kotlin.math.abs
import javax.inject.Inject

class PushHelper {

    private lateinit var activeSessionHolder: ActiveSessionHolder
    private var pushClient: AbsPush? = null
    private var hasReg = false
    private var hasBind = false
    private var brand = ""

    private val scope: CloseableCoroutineScope by lazy { CloseableCoroutineScope() }
    var retryCount = 0 //

    @RequiresApi(Build.VERSION_CODES.N)
    fun init() {
        if (hasReg) {
            Timber.v("已注册通知")
            if (Build.BRAND.equals("HUAWEI")) {
                initClient("huawei")
            }else if (Build.BRAND.lowercase().equals("vivo")) {
                initClient("vivo")
            }else if (Build.BRAND.lowercase().equals("oppo")) {
                initClient("oppo")
            }else if (Build.BRAND.lowercase().equals("xiaomi")||Build.BRAND.lowercase().equals("redmi")) {
                initClient("xiaomi")
            }else{
//                initClient("getui")
            }
            return
        }

        val input = PNSInput()
        input.model = Build.MODEL
        input.brand = Build.BRAND
        input.manufacturer = Build.MANUFACTURER
        input.sdk = Build.VERSION.SDK_INT.toString()
        input.osVersion = Build.VERSION.RELEASE
        input.rom = RomUtils.getRom() // room is a push identifier, used to determine which push to use
//        scope.launch(Dispatchers.IO) {
//            kotlin.runCatching {
//                val response = ApiService.getInstance().getPNS(input)
//                scope.launch(Dispatchers.Main) {
//                    if (response.isSuccess && response.obj != null && !TextUtils.isEmpty(response.obj!!.pns)) {
//                        AppCache.setPns(response.obj!!.pns)
//                        hasReg = true
//                        hasBind = false
//                        initClient(AppCache.getPNS())
//                    } else {
//                        if (!TextUtils.isEmpty(AppCache.getPNS())) {
//                            initClient(AppCache.getPNS())
//                        }
//                    }
//                }
//            }.exceptionOrNull()?.let {
//                Timber.v("获取通知PNS异常")
//                it.printStackTrace()
//            }
//        }
        if (input.brand.equals("HUAWEI")) {
            initClient("huawei")
        }else if (input.brand.lowercase().equals("vivo")) {
            initClient("vivo")
        }else if (Build.BRAND.lowercase().equals("oppo")) {
            initClient("oppo")
        }else if (Build.BRAND.lowercase().equals("xiaomi")||Build.BRAND.lowercase().equals("redmi")) {
            initClient("xiaomi")
        }else{
//            initClient("getui")
        }

        brand = input.brand
    }

    private fun initClient(type: String) {
        if (pushClient != null) {
            Timber.v("已有通知客户端")
            return
        }
        pushClient = when (type) {
            TYPE_HMS -> HWPush(BaseModule.getContext())
            TYPE_MIPUSH    -> MiPush(BaseModule.getContext())
            TYPE_OPPO_PUSH -> OppoPush(BaseModule.getContext())
            TYPE_VIVO_PUSH -> VivoPush(BaseModule.getContext())
//            TYPE_GETUI -> GeTuiPush(BaseModule.getContext())
            else -> FirebasePush(BaseModule.getContext())
//            else -> GeTuiPush(BaseModule.getContext())
        }
        try {
            pushClient?.init(BaseModule.getContext())
            pushClient?.startPush()
            clearNotification()
            bindDevice(pushClient?.regId)
            Timber.v("通知初始化完成")
        } catch (e: Exception) {
            Timber.v("通知初始化异常")
            e.printStackTrace()
        }
    }

    fun getBrand(): String {
        return brand.lowercase()
    }


    /**
     * bind the Matrix service and the push service through regId
     */
    fun bindDevice(regId : String?) {
        if (hasBind) {
            return
        }
        if (TextUtils.isEmpty(regId)) {
            return
        }
//            if (!UserCache.isLogin()) {
//                return
//            }
        try {
            val session = BaseModule.getSession()
            if (session == null) {
                return
            }

            val pushGateWay = BaseModule.getContext().getString(R.string.pusher_http_url)
            if (TextUtils.isEmpty(pushGateWay)) return
            val profileTag = "android_" + abs(session.myUserId.hashCode())
            var deviceDisplayName = session.sessionParams.deviceId
            if (TextUtils.isEmpty(deviceDisplayName)) {
                deviceDisplayName = "Android Mobile"
            }
            val locale = BaseModule.getContext().resources.configuration.locales[0]
            // "https://chat.yunify.com/_matrix/client/r0/_matrix/push/v1/notify"
            if (this.getPNS() == null || this.getPNS().equals("null")){
                return
            }
            val httpPusher = HttpPusher(
                    regId!!,
                    "android_${this.getPNS()}",
                    profileTag,
                    locale.language,
                    BaseModule.getContext().getString(R.string.app_name),
                    deviceDisplayName,
                    pushGateWay,
                    true,
                    "",//TODO 设备id
                    append = false,
                    withEventIdOnly = false
            )
            scope.launch {
                session.pushersService().addHttpPusher(httpPusher)
                hasBind = true
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun registerPusher() {
        if (hasBind) {
            return
        }

        init()
    }

    fun unregisterPusher() {
        val currentSession = activeSessionHolder.getSafeActiveSession() ?: return
        scope.launch {
            pushClient?.regId?.let {
                currentSession.pushersService().removeHttpPusher(it, "android_${AppCache.getPNS()}")
                hasBind = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun startPush(holder: ActiveSessionHolder) {
        activeSessionHolder = holder
//            if (!UserCache.isLogin()) {
//                return
//            }
        if (!DefaultSharedPreferences.getInstance(BaseModule.getContext())
                        .getBoolean(VectorPreferences.SETTINGS_ENABLE_THIS_DEVICE_PREFERENCE_KEY, true)) {
            Timber.v("设备未启动，停止初始化通知")
            return
        }
        if (pushClient == null) {
            Timber.v("开始初始化通知")
            init()
            return
        }
        Timber.v("已有通知客户端，开始推送")
        pushClient?.startPush()
    }

    fun stopPush() {
        if (pushClient == null) {
            return
        }
        clearNotification()
        pushClient?.stopPush()
        pushClient = null
    }

    fun logout() {
        stopPush()
        hasReg = false
        hasBind = false
    }

    fun getRegId(): String? {
        return if (pushClient == null) {
            null
        } else pushClient?.regId
    }

    fun getPNS(): String? {
        return if (pushClient == null) {
            null
        } else pushClient?.pns
    }

    fun setBadgeCount(context: Context?, count: Int) {
        if (pushClient == null) {
            return
        }
        pushClient?.setBadgeCount(context, count)
    }

    fun setBadge(context: Context?) {
        // val currentSession = activeSessionHolder.getSafeActiveSession() ?: return
        scope.launch {
//            val roomSummaries = withContext(Dispatchers.IO) {
//                val membershipList: MutableList<Membership> = ArrayList()
//                membershipList.add(Membership.JOIN)
//                val params = RoomSummaryQueryParams.Builder().apply {
//                    memberships = membershipList
//                }.build()
//                currentSession.getRoomSummaries(params)
//            }
//            if (roomSummaries.isEmpty()) {
//                YQBadgeUtils.setCount(0, context)
//            }
//            var count = 0
//            roomSummaries.forEach {
//                count += it.notificationCount
//            }
            val dmRooms = BaseModule.getSession().roomService().getNotificationCountForRooms(
                    roomSummaryQueryParams {
                        memberships = listOf(Membership.JOIN)
                        roomCategoryFilter = RoomCategoryFilter.ONLY_DM
//                        activeSpaceFilter = ActiveSpaceFilter.ActiveSpace(null)
                    }
            )

            val otherRooms = BaseModule.getSession().roomService().getNotificationCountForRooms(
                    roomSummaryQueryParams {
                        memberships = listOf(Membership.JOIN)
                        roomCategoryFilter = RoomCategoryFilter.ONLY_ROOMS
//                        activeSpaceFilter = ActiveSpaceFilter.ActiveSpace(null)
                    }
            )
            val count = dmRooms.totalCount + otherRooms.totalCount
            YQBadgeUtils.setCount(count, context)
        }
    }

    fun clickNotification(context: Context) {
        val intent = HomeActivity.newIntent(context, true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        context.startActivity(intent)
    }

    fun clearNotification() {
        if (pushClient == null) {
            return
        }
        pushClient?.clearPush()
    }

    companion object {
        private const val TYPE_HMS = "huawei"
        private const val TYPE_MIPUSH = "xiaomi"
        private const val TYPE_OPPO_PUSH = "oppo"
        private const val TYPE_VIVO_PUSH = "vivo"
        private const val TYPE_JIGUNAG_PUSH = "jiguang"
//        private const val TYPE_GETUI = "getui"
        private const val TYPE_FIREBASE = "firebase"

        private var INSTANCE: PushHelper? = null

        @JvmStatic
        fun getInstance() = INSTANCE ?: PushHelper().also { INSTANCE = it }


        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE?.scope?.close()
            INSTANCE = null
        }
    }
}
