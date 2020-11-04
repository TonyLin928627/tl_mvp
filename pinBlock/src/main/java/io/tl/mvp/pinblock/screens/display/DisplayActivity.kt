package io.tl.mvp.pinblock.screens.display

import android.content.Context
import android.content.Intent
import io.tl.mvp.lib.MvpActivity
import io.tl.mvp.pinblock.application.App
import io.tl.mvp.pinblock.application.PinBlockDataBridge
import io.tl.mvp.pinblock.screens.compute.PinBlockComputation

class DisplayActivity : MvpActivity<PinBlockDataBridge>() {
    override var doInject = {
        DaggerDisplayComponent.builder()
            .appComponent(App.appComponent)
            .displayModule(DisplayModule(this))
            .build().inject(this)
    }

    companion object{

        internal const val TAG = "DisplayActivity"

        internal const val EXTRA_PIN_BYTES = "EXTRA_PIN_BYTES"
        internal const val EXTRA_PAN_BYTES = "EXTRA_PAN_BYTES"
        internal const val EXTRA_PIN_BLOCK_BYTES = "EXTRA_PIN_BLOCK_BYTES"

        fun start(context: Context, data: PinBlockComputation){

            Intent(context, DisplayActivity::class.java).let { intent ->
                intent.putExtra(EXTRA_PIN_BYTES, data.first)
                intent.putExtra(EXTRA_PAN_BYTES, data.second)
                intent.putExtra(EXTRA_PIN_BLOCK_BYTES, data.third)

                context.startActivity(intent)
            }
        }
    }
}