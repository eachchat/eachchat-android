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

package im.vector.app.eachchat.push.vivo;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;

import android.os.Build;

import com.facebook.stetho.common.LogUtil;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;

import im.vector.app.R;
import im.vector.app.eachchat.base.BaseModule;
import im.vector.app.eachchat.push.AbsPush;
import im.vector.app.eachchat.push.PushHelper;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class VivoPush extends AbsPush {

    public String pns;
    public String regID;

    public VivoPush(Context context){
        super(context);
    }
    @Override
    public void init(Context context) {
        LogUtil.i("## vivo init");
        pns = "vivo";
        try{
            PushClient.getInstance(context).initialize();
        }catch (Exception e){
            LogUtil.i("## vivo 初始化异常");
            e.printStackTrace();
        }

    }

    @SuppressLint("CheckResult")
    @Override
    public void startPush() {
        LogUtil.i("## vivo startPush");
        Observable.create(emitter -> {
                    boolean result = true;
                    try {
                        // 打开push开关, 关闭为turnOffPush，详见api接入文档
                        PushClient.getInstance(BaseModule.getContext()).turnOnPush(new IPushActionListener() {
                            @Override
                            public void onStateChanged(int state) {
                                // TODO: 开关状态处理， 0代表成功
                                if (state==0){
                                    LogUtil.i("## vivo startPush ok");
                                    regID = PushClient.getInstance(BaseModule.getContext()).getRegId();
                                    LogUtil.i("## vivo startPush regID",regID);
                                    PushHelper.getInstance().bindDevice(regID);
                                }else {
                                    LogUtil.i("## vivo startPush faild");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        result = false;
                    }
                    emitter.onNext(result);
                }).subscribeOn(Schedulers.newThread())
                .subscribe(aBoolean -> {
                    LogUtil.i("## vivo init = ",aBoolean);
                });

    }

    @Override
    public void stopPush() {
        LogUtil.i("## vivo stopPush");
        PushClient.getInstance(BaseModule.getContext()).turnOffPush(null);
    }

    @Override
    public String getRegId() {
        return regID;
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
        LogUtil.i("## vivo clearNotifications");
        NotificationManager notificationManager = (NotificationManager)BaseModule.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
