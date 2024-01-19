package im.vector.app.eachchat.net;


import android.text.TextUtils;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import im.vector.app.eachchat.base.BaseModule;
import im.vector.app.eachchat.utils.TokenStore;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhouguanjie on 2019/8/9.
 */
public class HeaderInterceptor implements Interceptor {

    private HashMap<String, Object> headers;

    public HeaderInterceptor(HashMap<String, Object> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }
        HttpUrl url = requestBuilder.build().url();
        String requestPath = url.encodedPath();
        if (BaseModule.getSession() != null && TextUtils.equals(requestPath, "/api/services/auth/v1/logout") || TextUtils.equals(requestPath, "/api/services/auth/v1/token/refresh")) {
            if (!TextUtils.isEmpty(TokenStore.getRefreshToken(BaseModule.getSession()))) {
                requestBuilder.addHeader(NetConstant.AUTHORIZATION, String.format("%s %s", NetConstant.BEARER, TokenStore.getRefreshToken(BaseModule.getSession())));
            }
        } else if (!TextUtils.equals(requestPath, "/api/services/auth/v1/login")) {
            if (BaseModule.getSession() != null && !TextUtils.isEmpty(TokenStore.getAccessToken(BaseModule.getSession()))) {
                requestBuilder.addHeader(NetConstant.AUTHORIZATION, String.format("%s %s", NetConstant.BEARER, TokenStore.getAccessToken(BaseModule.getSession())));
            }
        }

        Request request = requestBuilder.build();
        Response.Builder responseBuilder = chain.proceed(request).newBuilder();
        return responseBuilder.build();
    }
}
