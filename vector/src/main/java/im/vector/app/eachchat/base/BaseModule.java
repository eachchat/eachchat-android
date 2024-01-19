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

package im.vector.app.eachchat.base;

import android.content.Context;

import org.matrix.android.sdk.api.session.Session;

import im.vector.app.core.di.ActiveSessionHolder;

/**
 * Created by zhouguanjie on 2019/8/23.
 */
public class BaseModule {

    private static Context mContext;

    private static Session mSession;

    public static void init(Context context) {
        if (mContext != null) return;
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setSession(Session session) {
        mSession = session;
    }

    public static Session getSession() {
        return mSession;
    }
}
