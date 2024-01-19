package im.vector.app.eachchat.net;

import android.text.TextUtils;

import java.io.IOException;

import im.vector.app.eachchat.utils.SPUtils;


/**
 * Created by zhouguanjie on 2019/8/23.
 */
public class NetConstant {

    public static final String BEARER = "Bearer";

    public static final String ACCESS_TOKEN = "Access-Token";

    public static final String REFRESH_TOKEN = "Refresh-Token";

    public static final String AUTHORIZATION = "Authorization";

    public static final String CONTENT_TYPE = "Content-type";

    public static final String APPLICATION_JSON = "application/json";

    public static final String ACCEPT = "Accept";

    public static final int NET_ERROR = -1;

    public static final int NET_SUCCESS = 200;
    public static final int NET_NO_NEWS = 502;
    public static final int VERSION_TOO_LOW = 534;

    private final static String KEY_SERVER_HOST = "key_server_host";
    private final static String KEY_MQTT_HOST = "key_mqtt_host";
    private final static String KEY_PUSH_HOST = "key_push_host";
    private final static String KEY_IDENTITY_HOST = "key_identity_host";

    public static final String USER_NAME = "client";
    public static final String PASS_WORD = "yiqiliao";

    public static String CONVERSATION_FILE_URL = getServerHostWithProtocol() + "/api/services/file/v1/conversation/%s/file/%s";
    public static final String DOWNLOAD_CONVERSATION_URL_PATH = "/api/services/file/v1/conversation/";

    public static String getConversationFileUrl(int conversationId, long fileId) {
        return String.format(NetConstant.CONVERSATION_FILE_URL, conversationId, fileId);
    }

    public static String getNewDownloadUrl(String timelineId, String originUrl) {
        if (TextUtils.isEmpty(timelineId)) return originUrl;
        else
            return String.format(getServerHostWithProtocol() + "/api/services/file/v1/dfs/download/%s", timelineId);
    }

    public static String getNewDownloadImageUrl(String timelineId, ImageType imageType, String originUrl) {
        if (TextUtils.isEmpty(timelineId)) return originUrl;
        else return String.format(imageType.url, timelineId);
    }

    public enum ImageType {
        ORIGIN(getServerHostWithProtocol() + "/api/services/file/v1/dfs/download/%s"),
        MIDDLE(getServerHostWithProtocol() + "/api/services/file/v1/dfs/thumbnail/" + "M/%s"),
        THUMBNAIL(getServerHostWithProtocol() + "/api/services/file/v1/dfs/thumbnail/" + "T/%s");

        private final String url;

        public String getUrl() {
            return url;
        }

        ImageType(String url) {
            this.url = url;
        }
    }


    public static String getServerHostWithProtocol() {
        String host = SPUtils.get(KEY_SERVER_HOST, "");
        if (TextUtils.isEmpty(host)) return "";
        if (host.startsWith("http://") || host.startsWith("https://")) {
            return host;
        } else {
            return "http://" + host;
        }
    }

    public static boolean isAppValid() {
        return !TextUtils.isEmpty(SPUtils.get(KEY_SERVER_HOST, ""));
    }

    public static String getMqttHostWithProtocol() {
        String mqttHost = SPUtils.get(KEY_MQTT_HOST, "");
        if (!TextUtils.isEmpty(mqttHost) && mqttHost.endsWith("/")) {
            mqttHost = mqttHost.substring(0, mqttHost.length() - 1);
        }
        return mqttHost;
    }

    public static void setServerHost(String host) {
        SPUtils.put(KEY_SERVER_HOST, host);
    }

    public static void setPushHost(String host) {
        SPUtils.put(KEY_PUSH_HOST, host);
    }

    public static String getPushHost() {
        return SPUtils.get(KEY_PUSH_HOST, "");
    }

    public static String getIdentityHost() {
        return SPUtils.get(KEY_IDENTITY_HOST, "");
    }

    public static void setIdentityHost(String host) {
        SPUtils.put(KEY_IDENTITY_HOST, host);
    }

    public static void setMqttServiceHost(String host) {
        if (host == null) host = "";
        if (host.endsWith("/")) host = host.substring(0, host.length() - 1);
        SPUtils.put(KEY_MQTT_HOST, host);
    }

    public static void clearHost() throws IOException {
        SPUtils.remove(KEY_SERVER_HOST);
        SPUtils.remove(KEY_MQTT_HOST);
        SPUtils.remove(KEY_PUSH_HOST);
        SPUtils.remove(KEY_IDENTITY_HOST);
        NetWorkManager.getInstance().update();
    }

}
