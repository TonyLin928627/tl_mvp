package co.spaceconnect.visitor

import android.app.Application
import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class AppScope

@AppScope
@Component (modules =  [DataBridgeModule::class] )
interface AppComponent{
    val signInDataBridge: SignInDataBridge
    fun inject(appContext: Application)
}

@Module
class DataBridgeModule {

    @AppScope
    @Provides
    fun signInDataBridge(): SignInDataBridge{
        return SignInDataBridge()
    }
}