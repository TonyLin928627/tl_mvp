package co.spaceconnect.visitor.screens.splash

import co.spaceconnect.visitor.App
import co.spaceconnect.visitor.SignInDataBridge
import io.tl.mvp.lib.MvpActivity
import kotlinx.android.synthetic.main.screen_splash.*

class SplashActivity : MvpActivity<SignInDataBridge>() {

    override var doInject = {
        this.appInfoTv
        DaggerSplashComponent.builder()
                .appComponent(App.appComponent)
                .splashModule(SplashModule(this))
                .build().inject(this)
    }
}