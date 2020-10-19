package co.spaceconnect.visitor.screens.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.spaceconnect.visitor.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_main)
    }

    companion object {
        fun start(context: Context, data: Bundle? = null) {
            Intent(context.applicationContext, MainActivity::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                data?.let { this.putExtras(it) }
                context.startActivity(this)
            }
        }
    }
}