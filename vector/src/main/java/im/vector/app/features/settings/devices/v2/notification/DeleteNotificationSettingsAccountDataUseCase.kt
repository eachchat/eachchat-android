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

package im.vector.app.features.settings.devices.v2.notification

import org.matrix.android.sdk.api.account.LocalNotificationSettingsContent
import org.matrix.android.sdk.api.session.Session
import javax.inject.Inject

/**
 * Delete the content of any associated notification settings to the current session.
 */
class DeleteNotificationSettingsAccountDataUseCase @Inject constructor(
        private val getNotificationSettingsAccountDataUseCase: GetNotificationSettingsAccountDataUseCase,
        private val setNotificationSettingsAccountDataUseCase: SetNotificationSettingsAccountDataUseCase,
) {

    suspend fun execute(session: Session) {
        val deviceId = session.sessionParams.deviceId
        if (getNotificationSettingsAccountDataUseCase.execute(session, deviceId)?.isSilenced != null) {
            val emptyNotificationSettingsContent = LocalNotificationSettingsContent(
                    isSilenced = null
            )
            setNotificationSettingsAccountDataUseCase.execute(session, deviceId, emptyNotificationSettingsContent)
        }
    }
}
