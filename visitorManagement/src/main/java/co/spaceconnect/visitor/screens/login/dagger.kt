package co.spaceconnect.visitor.screens.login

import co.spaceconnect.visitor.AppComponent
import co.spaceconnect.visitor.SignInDataBridge
import co.spaceconnect.visitor.screens.login.LoginActivity
import co.spaceconnect.visitor.screens.login.LoginModel
import co.spaceconnect.visitor.screens.login.LoginPresenter
import co.spaceconnect.visitor.screens.login.LoginView
import dagger.Component
import dagger.Module
import dagger.Provides
import io.tl.mvp.lib.MvpPresenter
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class LoginScope

@LoginScope
@Component(modules = [LoginModule::class], dependencies = [AppComponent::class])
interface LoginComponent {
    fun inject(activity: LoginActivity)
}

@Module
class LoginModule(private val activity: LoginActivity) {
    @Provides
    @LoginScope
    fun presenter(view: LoginView, model: LoginModel): MvpPresenter<SignInDataBridge> {
        return LoginPresenter(view, model, activity)
    }

    @Provides
    @LoginScope
    fun view(): LoginView {
        return LoginView()
    }

    @Provides
    @LoginScope
    fun model(signInDataBridge: SignInDataBridge): LoginModel {
        return LoginModel(signInDataBridge)
    }
}