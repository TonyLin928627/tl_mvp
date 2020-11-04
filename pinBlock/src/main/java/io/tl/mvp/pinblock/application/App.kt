package io.tl.mvp.pinblock.application

import android.app.Application
import dagger.Component
import dagger.Module
import dagger.Provides
import io.tl.mvp.lib.DataBridge
import javax.inject.Scope

//region Application
class App: Application() {

    companion object {
        val appComponent: AppComponent by lazy {
            DaggerAppComponent.builder().build()
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}
//endregion

//region AppComponent for dagger
@Scope
@Retention(AnnotationRetention.BINARY)
annotation class AppScope

@AppScope
@Component(modules =  [DataBridgeModule::class] )
interface AppComponent{
    val pinBlockDataBridge: PinBlockDataBridge
    fun inject(appContext: Application)
}

@Module
class DataBridgeModule {

    @AppScope
    @Provides
    fun signInDataBridge(): PinBlockDataBridge {
        return PinBlockDataBridge()
    }
}
//endregion

//region Global data bridge
class PinBlockDataBridge: DataBridge {

    val HardCodedPan = "1111222233334444";

}
//endregion