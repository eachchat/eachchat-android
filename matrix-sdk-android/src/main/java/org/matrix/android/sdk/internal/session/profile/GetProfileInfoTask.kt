/*
 * Copyright 2020 The Matrix.org Foundation C.I.C.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.matrix.android.sdk.internal.session.profile

import com.zhuinden.monarchy.Monarchy
import org.matrix.android.sdk.api.session.user.model.User
import org.matrix.android.sdk.api.util.JsonDict
import org.matrix.android.sdk.internal.di.SessionDatabase
import org.matrix.android.sdk.internal.network.GlobalErrorReceiver
import org.matrix.android.sdk.internal.network.executeRequest
import org.matrix.android.sdk.internal.session.user.UserEntityFactory
import org.matrix.android.sdk.internal.task.Task
import org.matrix.android.sdk.internal.util.awaitTransaction
import javax.inject.Inject

internal abstract class GetProfileInfoTask : Task<GetProfileInfoTask.Params, JsonDict> {
    data class Params(
            val userId: String,
            val storeInDatabase: Boolean = true,
    )
}

internal class DefaultGetProfileInfoTask @Inject constructor(
        private val profileAPI: ProfileAPI,
        private val globalErrorReceiver: GlobalErrorReceiver,
        @SessionDatabase private val monarchy: Monarchy,
) : GetProfileInfoTask() {

    override suspend fun execute(params: Params): JsonDict {
        return executeRequest(globalErrorReceiver) {
            profileAPI.getProfile(params.userId)
        }.also { user ->
            if (params.storeInDatabase) {
                // Insert into DB
                monarchy.awaitTransaction {
                    it.insertOrUpdate(UserEntityFactory.create(User.fromJson(params.userId, user)))
                }
            }
        }
    }
}
