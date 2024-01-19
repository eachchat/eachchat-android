package im.vector.app.eachchat.push.oppoPush;

import android.content.Context;

import com.facebook.stetho.common.LogUtil;
import com.heytap.msp.push.mode.DataMessage;
import com.heytap.msp.push.service.DataMessageCallbackService;

import im.vector.app.eachchat.push.PushHelper;

/**
 * Created by zhouguanjie on 2020/1/17.
 */
public class OppoPushServiceQ extends DataMessageCallbackService {

    @Override
    public void processMessage(Context context, DataMessage dataMessage) {
        super.processMessage(context, dataMessage);
        try {
            LogUtil.i("## oppo processMessage dataMessage = " + dataMessage.toString());
            PushHelper.getInstance().setBadge(context);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

