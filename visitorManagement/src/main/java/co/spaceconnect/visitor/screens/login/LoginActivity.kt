package co.spaceconnect.visitor.screens.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.spaceconnect.visitor.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_login)
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