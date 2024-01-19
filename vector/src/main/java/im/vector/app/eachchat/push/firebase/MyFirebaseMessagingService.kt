package im.vector.app.eachchat.push.firebase

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import im.vector.app.eachchat.push.PushHelper
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers

/**
 * Created by zhouguanjie on 2020/12/24.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onNewToken(token: String) {
        Observable.create { emitter: ObservableEmitter<Any?> ->
            PushHelper.getInstance().bindDevice(token)
            emitter.onNext(Any())
        }.subscribeOn(Schedulers.newThread())
                .subscribe()
    }

}
