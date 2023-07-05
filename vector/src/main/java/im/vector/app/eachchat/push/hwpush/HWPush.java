package im.vector.app.eachchat.push.hwpush;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;

import android.os.Build;

import com.facebook.stetho.common.LogUtil;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessaging;

import im.vector.app.R;
import im.vector.app.eachchat.base.BaseModule;
import im.vector.app.eachchat.push.AbsPush;
import im.vector.app.eachchat.push.PushHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhouguanjie on 2020/1/16.
 */
public class HWPush extends AbsPush {

    public HWPush(Context context) {
        super(context);
    }

    @SuppressLint("CheckResult")
    @Override
    public void init(Context context) {
        Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            boolean result = true;
            try {
                String token = HmsInstanceId.getInstance(context)
                        .getToken(context.getString(R.string.huawei_push_appid), "HMS");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    PushHelper.getInstance().bindDevice(token);
                }
            } catch (ApiException e) {
                e.printStackTrace();
                result = false;
            }
            emitter.onNext(result);
        }).subscribeOn(Schedulers.newThread())
                .subscribe(aBoolean -> {
                    LogUtil.i("## hw init appId = " + context.getString(R.string.huawei_push_appid));
                });
    }

    @Override
    public void startPush() {
        LogUtil.i("## hm startPush");
        HmsMessaging.getInstance(BaseModule.getContext()).turnOnPush();
    }

    @Override
    public void stopPush() {
        LogUtil.i("## hm stopPush");
        HmsMessaging.getInstance(BaseModule.getContext()).turnOffPush();
    }

    @Override
    public String getRegId() {
        return null;
    }

    @Override
    public void setBadgeCount(Context context, int count) {

    }

    @Override
    public void clearPush() {
        LogUtil.i("## hm clearNotification");
        NotificationManager notificationManager =
                (NotificationManager) BaseModule.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
