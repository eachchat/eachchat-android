package im.vector.app.eachchat.bean

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by zhouguanjie on 2021/1/4.
 */
@JsonClass(generateAdapter = true)
data class SearchInput(@Json(name = "filters") val filters: List<Filter>,
                       @Json(name = "perPage") val perPage: Int = 20,
                       @Json(name = "sortOrder") val sortOrder: Int = 1,
                       @Json(name = "sequenceId") val sequenceId: Int = 0) {
}
