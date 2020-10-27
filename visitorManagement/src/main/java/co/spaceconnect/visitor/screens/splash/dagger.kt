package co.spaceconnect.visitor.screens.splash

import co.spaceconnect.visitor.AppComponent
import co.spaceconnect.visitor.SignInDataBridge
import dagger.Component
import dagger.Module
import dagger.Provides
import io.tl.mvp.lib.DataBridge
import io.tl.mvp.lib.MvpModel
import io.tl.mvp.lib.MvpPresenter
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class SplashScope

@SplashScope
@Component(modules = [SplashModule::class], dependencies = [AppComponent::class])
interface SplashComponent {
    fun inject(activity: SplashActivity)
}

@Module
class SplashModule(private val activity: SplashActivity) {
    @Provides
    @SplashScope
    fun presenter(view: SplashView, model: SplashModel): MvpPresenter<SignInDataBridge> {
        return SplashPresenter(view, model, activity)
    }

    @Provides
    @SplashScope
    fun view(): SplashView {
        return SplashView()
    }

    @Provides
    @SplashScope
    fun model(signInDataBridge: SignInDataBridge): SplashModel {
        return SplashModel(signInDataBridge)
    }
}