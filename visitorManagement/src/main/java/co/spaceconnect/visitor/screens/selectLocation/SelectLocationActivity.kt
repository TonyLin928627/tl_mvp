package co.spaceconnect.visitor.screens.selectLocation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.spaceconnect.visitor.R

class SelectLocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_select_location)
    }

    companion object {

        fun start(context: Context, data: Bundle? = null) {
            Intent(context.applicationContext, SelectLocationActivity::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                data?.let { this.putExtras(it) }
                context.startActivity(this)
            }
        }
    }
}