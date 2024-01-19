package im.vector.app.eachchat.bean

import com.google.gson.annotations.SerializedName

/**
 * Created by chengww on 11/21/20
 * @author chengww
 */
data class WellKnownResult(
        @SerializedName("m.homeserver") val homeserver: Server?,
        @SerializedName("m.identity_server") val identityServer: Server?,
        @SerializedName("m.appserver") val appServer: Server?,
        @SerializedName("m.mqttserver") val mqttServer: Server?,
        @SerializedName("m.gms") val gms: GMS?,
        @SerializedName("m.push_gateway") val pushServer: Server?
)

data class Server(@SerializedName("base_url") val url: String?)

data class GMS(
        @SerializedName("base_url") val url: String?,
        val tid: String?
)

