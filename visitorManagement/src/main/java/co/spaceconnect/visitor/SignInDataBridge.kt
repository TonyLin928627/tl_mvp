package co.spaceconnect.visitor

import co.spaceconnect.visitor.screens.splash.ENTRY_TO_LOGIN_SCREEN
import dagger.Provides
import io.reactivex.Completable
import io.reactivex.Single
import io.tl.mvp.lib.DataBridge
import javax.inject.Inject
import javax.inject.Scope

class SignInDataBridge: DataBridge {

    fun cleanSignInData(): Completable {
        return Completable.complete()
    }

    fun getSignInStatus(): Single<Int> {
        return Single.just(ENTRY_TO_LOGIN_SCREEN);
    }
}