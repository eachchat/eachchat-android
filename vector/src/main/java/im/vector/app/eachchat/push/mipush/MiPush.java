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

package im.vector.app.eachchat.push.mipush;

import android.content.Context;

import com.facebook.stetho.common.LogUtil;
import com.xiaomi.mipush.sdk.MiPushClient;

import im.vector.app.R;
import im.vector.app.eachchat.push.AbsPush;
import im.vector.app.eachchat.push.PushHelper;

/**
 * Created by zhouguanjie on 2020/1/10.
 */
public class MiPush extends AbsPush {
    private Context mContext;
    public String pns;

    public MiPush(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void init(Context context) {
        MiPushClient.registerPush(context,
                context.getString(R.string.mipush_appid),
                context.getString(R.string.mipush_appkey));
        LogUtil.i("## mi appId:" + context.getString(R.string.mipush_appid));
        MiPushClient.enablePush(mContext);
        String regId = MiPushClient.getRegId(mContext);
        LogUtil.i("## mi getRegId = " + regId);
        PushHelper.getInstance().bindDevice(regId);
        pns="xiaomi";
    }

    @Override
    public void startPush() {
        LogUtil.i("## mi startPush");
        MiPushClient.enablePush(mContext);
    }

    @Override
    public void stopPush() {
        LogUtil.i("## mi stopPush");
        MiPushClient.disablePush(mContext);
    }

    @Override
    public String getRegId() {
        LogUtil.i("## mi getRegId = " + MiPushClient.getRegId(mContext));
        return MiPushClient.getRegId(mContext);
    }

    @Override
    public String getPNS() {
        return pns;
    }

    @Override
    public void setBadgeCount(Context context, int count) {

    }

    @Override
    public void clearPush() {
        LogUtil.i("## mi clearNotification");
        MiPushClient.clearNotification(mContext);
    }
}
