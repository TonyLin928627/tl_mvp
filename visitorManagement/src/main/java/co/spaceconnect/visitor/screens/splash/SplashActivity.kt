package co.spaceconnect.visitor.screens.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.tl.mvp.lib.MvpActivity

class SplashActivity : MvpActivity() {

    override var doInject = {
        DaggerSplashComponent.builder()
            .splashModule(SplashModule(this))
            .build().inject(this)
    }
}