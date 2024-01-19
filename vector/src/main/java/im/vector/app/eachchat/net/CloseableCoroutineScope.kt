package im.vector.app.eachchat.net

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import java.io.Closeable
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Created by chengww on 1/26/21
 * @author chengww
 */
 class CloseableCoroutineScope : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = EmptyCoroutineContext

    override fun close() {
        coroutineContext.cancel()
    }
}
