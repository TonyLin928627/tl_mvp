package co.spaceconnect.visitor

import co.spaceconnect.visitor.screens.splash.ENTRY_TO_LOGIN_SCREEN
import io.reactivex.Completable
import io.reactivex.Single
import io.tl.mvp.lib.DataBridge

class SignInDataBridge : DataBridge {

    fun cleanSignInData(): Completable {
        return Completable.complete()
    }

    fun getSignInStatus(): Single<Int> {
        return Single.just(ENTRY_TO_LOGIN_SCREEN);
    }
}