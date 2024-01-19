package im.vector.app.eachchat.push.oppoPush;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import com.facebook.stetho.common.LogUtil;
import com.heytap.msp.push.HeytapPushManager;
import com.heytap.msp.push.callback.ICallBackResultService;
import com.heytap.msp.push.notification.PushNotificationManager;

import im.vector.app.R;
import im.vector.app.eachchat.push.AbsPush;
import im.vector.app.eachchat.push.PushHelper;

/**
 * Created by zhouguanjie on 2020/1/16.
 */
public class OppoPush extends AbsPush {
    String pns="";

    public OppoPush(Context context) {
        super(context);
    }

    @Override
    public void init(Context context) {
        HeytapPushManager.register(context, context.getString(R.string.oppo_appkey),
                context.getString(R.string.oppo_secret), new ICallBackResultService() {
                    @Override
                    public void onRegister(int responseCode, String registerID) {
                        this.setUpNotificationChannels(context);

                        pns="oppo";
                        // 注册的结果,如果注册成功,registerID就是客户端的唯一身份标识
                        LogUtil.i("## oppo onRegister responseCode = " + responseCode + " registerID = " + registerID);
                        if (responseCode == 0) {
                            PushHelper.getInstance().bindDevice(registerID);
                        }
                    }

                    @Override
                    public void onUnRegister(int responseCode) {
                        // 反注册的结果
                        LogUtil.i("## oppo onUnRegister");
                    }

                    @Override
                    public void onSetPushTime(int responseCode, String pushTime) {
                        // 获取设置推送时间的执行结果
                        LogUtil.i("## oppo onSetPushTime");
                    }

                    @Override
                    public void onGetPushStatus(int responseCode, int status) {
                        // 获取当前的push状态返回,根据返回码判断当前的push状态,返回码具体含义可以参考[错误码]
                        LogUtil.i("## oppo onGetPushStatus");
                    }

                    @Override
                    public void onGetNotificationStatus(int responseCode, int status) {
                        // 获取当前通知栏状态，返回码具体含义可以参考[错误码]
                        LogUtil.i("## oppo onGetNotificationStatus");
                    }

                    @Override
                    public void onError(int code, String msg) {
                        // 错误码返回的接口(当前主要是用于调用频繁的回调，后续可做拓展)
                        LogUtil.i("## oppo onError");
                    }

                    public void setUpNotificationChannels(Context context) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel("oppopush","即时消息",NotificationManager.IMPORTANCE_HIGH);
                            channel.setDescription("即时消息推送通道");
                            // 设置桌面角标
                            channel.enableLights(true);
                            // 角标颜色
                            channel.setLightColor(Color.RED);
                            // 长按图标显示显示通道明细
                            channel.setShowBadge(true);
                            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                            notificationManager.createNotificationChannel(channel);
                        }
                    }
                });

    }

    @Override
    public void startPush() {
        LogUtil.i("## oppo startPush");
        HeytapPushManager.resumePush();
    }

    @Override
    public void stopPush() {
        LogUtil.i("## oppo stopPush");
//        HeytapPushManager.pausePush();
    }

    @Override
    public String getRegId() {
        LogUtil.i("## oppo getRegId = " + HeytapPushManager.getRegisterID());
        return HeytapPushManager.getRegisterID();
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
        LogUtil.i("## oppo clearNotifications");
        HeytapPushManager.clearNotifications();
    }
}
