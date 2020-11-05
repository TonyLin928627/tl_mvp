package co.spaceconnect.visitor.screens.login


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.IntDef
import butterknife.BindView
import butterknife.ButterKnife
import co.spaceconnect.visitor.R
import co.spaceconnect.visitor.R.*
import co.spaceconnect.visitor.R.id.appInfoTv
import co.spaceconnect.visitor.SignInDataBridge
import co.spaceconnect.visitor.displayBuildInfo
import co.spaceconnect.visitor.screens.login.LoginActivity
import co.spaceconnect.visitor.screens.main.MainActivity
import co.spaceconnect.visitor.screens.selectLocation.SelectLocationActivity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.tl.mvp.lib.*
import io.tl.mvp.lib.views.MvpToolbarWithoutDrawerView
import java.util.concurrent.TimeUnit

//region model

private const val TAG = "LoginScreen"
class LoginModel(dataBridge: SignInDataBridge): MvpModel<SignInDataBridge>(dataBridge) {

}
//endregion

//region view
class LoginView : MvpView(){
    override val contentLayoutId = layout.screen_login

    @BindView(R.id.emailEt)
    lateinit var emailEt: EditText

    @BindView(R.id.passwordEt)
    lateinit var passwordEt: EditText

    @BindView(R.id.signInBtn)
    lateinit var signInBtn: Button

    val onSignInBtnClicked by lazy { signInBtn.clickThrottle() }

    override fun initViews() {
        super.initViews()

        ButterKnife.bind(this, screenContainer)
    }
}

//endregion

//region presenter
class LoginPresenter(override val mvpView: LoginView,
                      override val mvpModel: LoginModel,
                      override val activity: LoginActivity
) : MvpPresenter<SignInDataBridge>(){

    override val allObservables by lazy {
        listOf<Disposable>(
            mvpView.onSignInBtnClicked.subscribe{this.doOnSignInBtnClicked(mvpView.emailEt.text.toString(), mvpView.passwordEt.text.toString())}
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun doOnSignInBtnClicked (email: String,  password: String){

        mvpView.hideKeyboard(activity)
        mvpView.showAlertMsg(alertTitle = "Login Clicked", "$email, $password")
    }
}

//endregion