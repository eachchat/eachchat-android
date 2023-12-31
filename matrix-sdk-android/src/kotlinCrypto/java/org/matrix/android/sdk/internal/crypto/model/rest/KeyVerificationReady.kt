/*
 * Copyright 2020 The Matrix.org Foundation C.I.C.
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
package org.matrix.android.sdk.internal.crypto.model.rest

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.matrix.android.sdk.api.session.crypto.model.SendToDeviceObject
import org.matrix.android.sdk.api.session.events.model.toContent
import org.matrix.android.sdk.internal.crypto.verification.VerificationInfoReady

/**
 * Requests a key verification with another user's devices.
 */
@JsonClass(generateAdapter = true)
internal data class KeyVerificationReady(
        @Json(name = "from_device") override val fromDevice: String?,
        @Json(name = "methods") override val methods: List<String>?,
        @Json(name = "transaction_id") override val transactionId: String? = null
) : SendToDeviceObject, VerificationInfoReady {

    override fun toSendToDeviceObject() = this

    override fun toEventContent() = toContent()
}
