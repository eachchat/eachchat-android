package im.vector.app.eachchat.push.getui;

import android.app.NotificationManager;
import android.content.Context;

import com.facebook.stetho.common.LogUtil;

import im.vector.app.eachchat.base.BaseModule;
import im.vector.app.eachchat.push.AbsPush;


/**
 * Created by zhouguanjie on 2021/3/1.
 */
public class GeTuiPush extends AbsPush {

    public GeTuiPush(Context context) {
        super(context);
    }

    @Override
    public void init(Context context) {
        LogUtil.i("## GeTui init");
    }

    @Override
    public void startPush() {
        LogUtil.i("## GeTui startPush");
    }

    @Override
    public void stopPush() {
        LogUtil.i("## GeTui stopPush");
    }

    @Override
    public String getRegId() {
        return "";
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
