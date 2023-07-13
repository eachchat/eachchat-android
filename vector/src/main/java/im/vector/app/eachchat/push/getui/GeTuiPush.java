package im.vector.app.eachchat.push.getui;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;

import com.facebook.stetho.common.LogUtil;

import com.igexin.sdk.GetuiPushException;
import com.igexin.sdk.IUserLoggerInterface;
import com.igexin.sdk.PushManager;

import com.vivo.push.IPushActionListener;
import com.vivo.push.PushClient;

import im.vector.app.eachchat.base.BaseModule;
import im.vector.app.eachchat.push.AbsPush;
import im.vector.app.eachchat.push.PushHelper;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Created by zhouguanjie on 2021/3/1.
 */
public class GeTuiPush extends AbsPush {
    public String pns = "";
    public String regID = "";

    public GeTuiPush(Context context) {
        super(context);
    }

    @SuppressLint("CheckResult")
    @Override
    public void init(Context context) {
        LogUtil.i("## GeTui init");
        Observable.create(emitter -> {
                    boolean result = true;
                    try {
                        // getui
                        PushManager.getInstance().initialize(context);
                        com.igexin.sdk.PushManager.getInstance().setDebugLogger(context, new IUserLoggerInterface() {
                            @Override
                            public void log(String s) {
                                LogUtil.i("PUSH_LOG", s);
                            }
                        });
                        regID=PushManager.getInstance().getClientid(context);
                        pns="getui";
                        try {
                            PushManager.getInstance().checkManifest(context);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } catch (GetuiPushException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        result = false;
                    }
                    emitter.onNext(result);
                }).subscribeOn(Schedulers.newThread())
                .subscribe(aBoolean -> {
                    LogUtil.i("## getui init = ",aBoolean);
                });
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
        return PushManager.getInstance().getClientid(BaseModule.getContext());
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
        LogUtil.i("## GeTui clearNotifications");
        NotificationManager notificationManager =
                (NotificationManager) BaseModule.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
