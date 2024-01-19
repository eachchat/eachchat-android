package im.vector.app.eachchat.bean

import java.io.Serializable

/**
 * Created by zhouguanjie on 2019/8/23.
 */
class Response<V, T> : Serializable {
    var code = 0
    var message: String? = null
    var obj: V? = null
        private set
    var results: T? = null
        private set
    var total = 0
    var hasNext = false
    fun setObj(obj: V) {
        this.obj = obj
    }

    fun setResults(results: T) {
        this.results = results
    }

    val isSuccess: Boolean
        get() = code == 200 || code == 502
}
