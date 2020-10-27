package co.spaceconnect.visitor.screens.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.spaceconnect.visitor.App
import co.spaceconnect.visitor.R
import co.spaceconnect.visitor.SignInDataBridge
import io.tl.mvp.lib.MvpActivity

class LoginActivity : MvpActivity<SignInDataBridge>() {

    override var doInject = {
        DaggerLoginComponent.builder()
            .appComponent(App.appComponent)
            .loginModule(LoginModule(this))
            .build().inject(this)
    }

    companion object {

        fun start(context: Context, data: Bundle? = null) {
            Intent(context.applicationContext, LoginActivity::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                data?.let { this.putExtras(it) }
                context.startActivity(this)
            }
        }
    }


}