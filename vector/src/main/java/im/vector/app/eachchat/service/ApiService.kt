package im.vector.app.eachchat.service

import im.vector.app.eachchat.bean.PNSInput
import im.vector.app.eachchat.bean.PNSOutput
import im.vector.app.eachchat.bean.Response
import im.vector.app.eachchat.net.NetWorkManager
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/services/auth/v1/pns")
    suspend fun getPNS(@Body model: PNSInput?): Response<PNSOutput?, Any?>

    companion object {
        fun getInstance(): ApiService {
            return NetWorkManager.getInstance().getRetrofit().create(ApiService::class.java)
        }
    }
}
