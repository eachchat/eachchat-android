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

package im.vector.app.eachchat.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;


import java.util.ArrayList;
import java.util.List;

import im.vector.app.BuildConfig;
import im.vector.app.R;

/**
 * Created by zhouguanjie on 2020/1/10.
 */
public abstract class AbsPush {

    private final static String VIDEO_CHANNEL = "100";
    private final static String VIDEO_CHANNEL_GROUP = "Video";
    private final static String CHAT_CHANNEL_GROUP = "Chat";
    public final static String CHAT_CHANNEL_ID = "0";
    private final static String LISTENING_FOR_EVENTS_NOTIFICATION_CHANNEL_ID = "LISTEN_FOR_EVENTS_NOTIFICATION_CHANNEL_ID";
    private final static String NOISY_NOTIFICATION_CHANNEL_ID = "DEFAULT_NOISY_NOTIFICATION_CHANNEL_ID";
    private final static String SILENT_NOTIFICATION_CHANNEL_ID = "DEFAULT_SILENT_NOTIFICATION_CHANNEL_ID_V2";
    private final static String CALL_NOTIFICATION_CHANNEL_ID = "CALL_NOTIFICATION_CHANNEL_ID_V2";

    public AbsPush(Context context) {
        initChannel(context);
    }

    public abstract void init(Context context);

    public abstract void startPush();

    public abstract void stopPush();

    public abstract String getRegId();

    public abstract String getPNS();


    public abstract void setBadgeCount(Context context, int count);

    public abstract void clearPush();

    public void initChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            List<NotificationChannel> channels = new ArrayList<>();
            // todo 通知要怎么与 element 兼容?
            if (nm != null) {
//                createVideoChannel(context, nm, channels);
//                createChatChannel(context, nm, channels);
                nm.createNotificationChannels(channels);
            }
        }
    }

//    private void createVideoChannel(Context context, NotificationManager nm, List<NotificationChannel> channels) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//            return;
//        }
//        NotificationChannel notificationChannel =
//                new NotificationChannel(VIDEO_CHANNEL, context.getString(R.string.audio_and_video_notification), NotificationManager.IMPORTANCE_HIGH);
//        notificationChannel.enableLights(true);
//        notificationChannel.enableVibration(true);
//        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);//锁屏可见
//        notificationChannel.setShowBadge(true);//展示角标
////        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
////                + "://" + BuildConfig.APPLICATION_ID + "/raw/avchat_ringing.ogg");
////        notificationChannel.setSound(uri, null);
//        notificationChannel.setShowBadge(true);
//        channels.add(notificationChannel);
//    }

//    private void createChatChannel(Context context, NotificationManager nm, List<NotificationChannel> channels) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//            return;
//        }
//        NotificationChannel notificationChannel = new NotificationChannel(CHAT_CHANNEL_ID, context.getString(R.string.message_notification), NotificationManager.IMPORTANCE_HIGH);
//        notificationChannel.enableLights(true);
//        notificationChannel.enableVibration(true);
//        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);//锁屏可见
//        notificationChannel.setShowBadge(true);//展示角标
////        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
////                + "://" + BuildConfig.APPLICATION_ID + "/raw/notify.amr");
////        notificationChannel.setSound(uri, null);
//        notificationChannel.setShowBadge(true);
//        channels.add(notificationChannel);
//    }
}
