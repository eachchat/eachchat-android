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

package im.vector.app.eachchat.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.matrix.android.sdk.api.session.events.model.Content
import org.matrix.android.sdk.api.session.events.model.UnsignedData

/**
 * Created by zhouguanjie on 2021/1/4.
 */
@JsonClass(generateAdapter = true)
data class Filter(@Json(name = "field") val field: String,
                  @Json(name = "operator") val operator: String = "co",
                  @Json(name = "value") val value: String,
                  @Json(name = "logic") val logic: Int = 1)

@JsonClass(generateAdapter = true)
data class GroupFilter(@Json(name = "filters") val filters: List<Filter>, @Json(name = "field") val field: String = "room_id")

@JsonClass(generateAdapter = true)
data class SearchGroupCountInput(
        @Json(name = "limit") val limit: Int = SEARCH_TYPE_COUNT,
        @Json(name = "groups") val groups: List<GroupFilter>,
)

const val SEARCH_TYPE_COUNT = 3


@JsonClass(generateAdapter = true)
data class SearchGroupMessageResponse(
        @Json(name = "room_id") val roomId: String?,
        @Json(name = "origin_server_ts") val origin_server_ts: Long?,
        @Json(name = "body") val body: String?,
        @Json(name = "event") val event: Event?)

data class Event(
        @Json(name = "type") val type: String? = null,
        @Json(name = "event_id") val event_id: String? = null,
        @Json(name = "content") val content: Content? = null,
        @Json(name = "prev_content") val prev_content: Content? = null,
        @Json(name = "origin_server_ts") val origin_server_ts: Long? = null,
        @Json(name = "sender") val sender: String? = null,
        @Json(name = "state_key") val state_key: String? = null,
        @Json(name = "room_id") val room_id: String? = null,
        @Json(name = "unsigned") val unsigned: UnsignedData? = null,
        @Json(name = "redacts") val redacts: String? = null
)

@JsonClass(generateAdapter = true)
data class SearchGroupCountResponse(@Json(name = "rooms") val rooms: SearchRoomsData?)

@JsonClass(generateAdapter = true)
data class SearchRoomsData(
        @Json(name = "more") val more: Boolean?,
        @Json(name = "room_count") val roomCount: Int,
        @Json(name = "results") val results: List<GroupCountData>?
)

@JsonClass(generateAdapter = true)
data class GroupCountData(
        @Json(name = "room_id") val room_id: String?,
        @Json(name = "keywordCount") val keywordCount: Int,
        @Json(name = "firstChat") val firstChat: SearchGroupMessageResponse?)
