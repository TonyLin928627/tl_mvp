package co.spaceconnect.visitor.screens.splash

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
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
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.tl.mvp.lib.*
import io.tl.mvp.lib.views.MvpToolbarWithoutDrawerView
import java.util.concurrent.TimeUnit

//region model
typealias ScreenEntryMethod = (activity: Activity, data: Bundle?)->Unit

const val ENTRY_TO_LOGIN_SCREEN = 1
const val ENTRY_TO_LOCATION_SELECT_SCREEN = 2
const val ENTRY_TO_MAIN_SCREEN = 3

@Retention(AnnotationRetention.SOURCE)
@IntDef(flag = true, value = [ENTRY_TO_LOGIN_SCREEN, ENTRY_TO_LOCATION_SELECT_SCREEN, ENTRY_TO_MAIN_SCREEN])
annotation class ScreenEntry

private const val TAG = "SplashScreen"
class SplashModel(dataBridge: SignInDataBridge): MvpModel<SignInDataBridge>(dataBridge) {


    fun getEntry(): Single<ScreenEntryMethod> {

        return (dataBridge).getSignInStatus().flatMap {entry->
            when (entry) {
                ENTRY_TO_LOGIN_SCREEN -> {
                    Single.just<ScreenEntryMethod>(LoginActivity.Companion::start)
                }
                ENTRY_TO_LOCATION_SELECT_SCREEN -> {
                    Single.just<ScreenEntryMethod>(SelectLocationActivity.Companion::start)
                }
                ENTRY_TO_MAIN_SCREEN -> {
                    Single.just<ScreenEntryMethod>(MainActivity.Companion::start)
                }

                else-> throw IllegalArgumentException("Illegal argument : $entry")
            }
        }
    }


}
//endregion

//region view
class SplashView : MvpView(){
    override val contentLayoutId = layout.screen_splash

    @BindView(id.appInfoTv)
    lateinit var appInfoTv: TextView

    override fun initViews() {
        super.initViews()

        ButterKnife.bind(this, screenContainer)
        appInfoTv.displayBuildInfo(getContext())

    }
}

//endregion

//region presenter
private const val MIN_DISPLAY_TIME = 3L
class SplashPresenter(override val mvpView: SplashView,
                      override val mvpModel: SplashModel,
                      override val activity: SplashActivity) : MvpPresenter<SignInDataBridge>(){

    override val allObservables = emptyList<Disposable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //zip this single to ensure the splash screen will be shown for at least 3 seconds
        val minThreeSecondsSingle = Single.timer(MIN_DISPLAY_TIME, TimeUnit.SECONDS)

        val disposable = Single.zip(
            mvpModel.getEntry(), minThreeSecondsSingle,
            { screenEntry, _ -> screenEntry}
        )
            .subscribeOn(Schedulers.io())
            .subscribe(
                {screenEntrya->
                    screenEntrya.invoke(activity, null)
                    activity.finish()
                },

                {err->
                    mvpView.showErrorMsg(err){activity.finish()}
                }

            )

        addDisposable(disposable)
    }
}

//endregion