package io.tl.mvp.pinblock.screens.compute

import io.tl.mvp.lib.MvpActivity
import io.tl.mvp.pinblock.application.App
import io.tl.mvp.pinblock.application.PinBlockDataBridge

class ComputeActivity : MvpActivity<PinBlockDataBridge>() {

    override var doInject = {
        DaggerMainComponent.builder()
            .appComponent(App.appComponent)
            .computeModule(ComputeModule((this)))
            .build().inject(this)
    }

    companion object{
        internal const val TAG = "ComputeScreen"
    }
}

