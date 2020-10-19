package co.spaceconnect.visitor.screens.splash

import co.spaceconnect.visitor.SignInDataBridge
import dagger.Component
import dagger.Module
import dagger.Provides
import io.tl.mvp.lib.MvpModel
import io.tl.mvp.lib.MvpPresenter
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class SplashScope

@SplashScope
@Component(modules = [SplashModule::class])
interface SplashComponent {
    fun inject(activity: SplashActivity)
}

@Module
class SplashModule(private val activity: SplashActivity) {
    @Provides
    @SplashScope
    fun presenter(view: SplashView, model: SplashModel): MvpPresenter {
        return SplashPresenter(view, model, activity)
    }

    @Provides
    @SplashScope
    fun view(): SplashView {
        return SplashView()
    }

    @Provides
    @SplashScope
    fun model(): SplashModel {
        return SplashModel(SignInDataBridge())
    }

}