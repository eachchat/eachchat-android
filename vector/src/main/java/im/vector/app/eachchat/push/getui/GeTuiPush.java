package im.vector.app.eachchat.push.getui;

import android.app.NotificationManager;
import android.content.Context;

import com.facebook.stetho.common.LogUtil;

import im.vector.app.eachchat.base.BaseModule;
import im.vector.app.eachchat.push.AbsPush;

import com.igexin.sdk.PushManager;

public class GeTuiPush extends AbsPush {

    public String regID;
    private android.content.Context Context;

    public GeTuiPush(Context context) {
        super(context);
    }

    @Override
    public void init(Context context) {
        this.Context = context;
        PushManager.getInstance().initialize(context);
        this.regID =  PushManager.getInstance().getClientid(context);
        LogUtil.i("## GeTui init");
    }

    @Override
    public void startPush() {
        PushManager.getInstance().turnOnPush(Context);
        LogUtil.i("## GeTui startPush");
    }

    @Override
    public void stopPush() {
        PushManager.getInstance().turnOffPush(Context);
        LogUtil.i("## GeTui stopPush");
    }

    @Override
    public String getRegId() {
        return this.regID;
    }

    @Override
    public String getPNS() {
        return "GETUI";
    }

    @Override
    public void setBadgeCount(Context context, int count) {

    }

    @Override
    public void clearPush() {
        LogUtil.i("## GeTui clearNotifications");
        NotificationManager notificationManager =
                (NotificationManager) BaseModule.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
