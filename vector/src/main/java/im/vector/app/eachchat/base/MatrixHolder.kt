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

package im.vector.app.eachchat.base

import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.session.content.ContentUrlResolver
import java.util.concurrent.atomic.AtomicReference

/**
 * Created by chengww on 2020/10/10
 *
 *
 * @author chengww
 */
class MatrixHolder private constructor() {


    interface SessionCallback {
        fun setSession(session: Session)
        fun clearSession()
    }

    var sessionCallback: SessionCallback? = null

    var activeSession: AtomicReference<Session?> = AtomicReference()

    fun setSession(session: Session) {
        activeSession.set(session)
        sessionCallback?.setSession(session)
    }

    fun clearSession() {
        activeSession.set(null)
        sessionCallback?.clearSession()
    }

    fun getSession() = activeSession.get()

    companion object {
        private var INSTANCE: MatrixHolder? = null

        @JvmStatic
        fun getInstance() = INSTANCE ?: MatrixHolder().also { INSTANCE = it }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }

        @JvmStatic
        fun getScaleUrl(avatarUrl: String?) =
                INSTANCE?.getSession()?.contentUrlResolver()?.resolveThumbnail(avatarUrl, 250, 250, ContentUrlResolver.ThumbnailMethod.SCALE)
                        ?: avatarUrl

        @JvmStatic
        fun getFullUrl(avatarUrl: String?) =
                INSTANCE?.getSession()?.contentUrlResolver()?.resolveFullSize(avatarUrl)
                        ?: avatarUrl

        fun getUserId(): String? {
            return INSTANCE?.getSession()?.myUserId
        }
    }
}
