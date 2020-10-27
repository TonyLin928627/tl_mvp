package io.tl.mvp.lib

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.ContactsContract
import android.util.Log
import android.view.KeyEvent
import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.tl.mvp.lib.menu.MenuItem
import io.tl.mvp.lib.menu.MenuItemType
import io.tl.mvp.lib.toolbar.Toolbar
import io.tl.mvp.lib.toolbar.ToolbarWithDrawer

//region base presenter definitions
abstract class MvpPresenter<T : DataBridge>{

    abstract val mvpView: MvpView
    abstract val mvpModel:  MvpModel<T, T>
    abstract val activity: MvpActivity<T>

    private val compositeDisposable = CompositeDisposable()

    private var isIgnoreMenuItemClicked: Boolean = false

    fun addDisposable(vararg d: Disposable){
        d.forEach {
            compositeDisposable.add(it)
        }
    }

    open fun onToolbarStartBtnClick(){
        onBackPressed()
    }

    @CallSuper
    open fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {}

    private fun getContext(): Context {
        return this.activity
    }

    @CallSuper
    open fun onCreate(savedInstanceState: Bundle?){

        mvpView.doInit(getContext = this::getContext)

        activity.let { theActivity ->
            theActivity.setContentView(mvpView.screenContainer)

            allObservables.forEach {
                addDisposable(it)
            }

            if (mvpView is Toolbar) {
                (mvpView as Toolbar).let { viewWithToolbar ->

                    viewWithToolbar.initToolbar(theActivity.resources)

                    compositeDisposable.addAll(
                        viewWithToolbar.toolbarStartBtnObs().subscribe { onToolbarStartBtnClick()}
                    )


                    if (viewWithToolbar is ToolbarWithDrawer) {

                        (viewWithToolbar).let { viewWithToolbarAndDrawer ->

                            viewWithToolbarAndDrawer.populateMenuItems(this::getMenuItems)

                            viewWithToolbar.onSignOutBtnClicked().subscribe{doOnSignOutBtnClicked?.invoke()}.let { addDisposable(it) }

                            compositeDisposable.addAll(

                                viewWithToolbarAndDrawer.onMenuItemClicked
                                    .doOnSubscribe { addDisposable(it) }
                                    .doOnDispose {
                                        Log.d("onMenuItemClicked", mvpView.javaClass.name + ": doOnDispose")
                                    }
                                    .switchMap {
                                        Observable.just(it)
                                    }
                                    .subscribe {menuItemIndex->
                                        if (!isIgnoreMenuItemClicked) {
                                            this.doOnMenuItemClicked(menuItemIndex)
                                        }
                                    },

                                viewWithToolbar.toolbarEndBtnObs()?.subscribe {
                                    viewWithToolbarAndDrawer.openDrawer()
                                }
                            )
                        }
                    }
                }

            }
        }

        if (!mvpModel.isConnectedToNetwork(activity)) {
            this.mvpView.showErrorMsgWithTitle("Network not available, please try later"){this.onBackPressed()}
        }
    }

    open fun getMenuItems(): Single<List<MenuItem>> {
        return Single.just(emptyList())
    }

    /**
     * @return return the passed in menu item if it has not been consumed by the method, otherwise return null
     */
    open fun doOnMenuItemClicked(aMenuItem: MenuItem){

        when(aMenuItem.menuItemType) {
            MenuItemType.HOME -> {
                when(this){
//                    is MainScreenPresenter -> MainScreenActivity.startWithoutReload(activity)
//
//                    else -> MainScreenActivity.startWithSingleTask(activity)
                }

            }

            else -> {
//                MainScreenActivity.startActivityFromDrawerMenu(activity, aMenuItem)
            }
        }
    }

    open fun onNewIntent(intent: Intent?) {}

    @CallSuper
    open fun onStart() {
        isIgnoreMenuItemClicked=false
    }

    @CallSuper
    open fun onRestart() {}

    @CallSuper
    open fun onResume() {}

    @CallSuper
    open fun onPause() {}

    @CallSuper
    open fun onStop() {
        isIgnoreMenuItemClicked = true
    }

    @CallSuper
    open fun onSaveInstanceState(outState: Bundle) {}

    @CallSuper
    open fun onRestoreInstanceState(savedInstanceState: Bundle?) {}

    @CallSuper
    open fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {}

    @CallSuper
    open fun onDestroy() {
        compositeDisposable.clear()

        activity.presenter == null
        mvpView.doDeinit()
    }

    open fun onBackPressed(): Boolean {
        activity.finish()
        return true
    }

    @CallSuper
    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {

        return false;
//        return when (requestCode){
//
//            SignOutConfirmationActivity.SIGN_OUT_REQUEST_CODE -> {
//
//                val decision = data?.getBooleanExtra(AppConstants.CONFIRMATION_SCREEN_RESULT, false) ?:  false
//
//                if (decision){
//                    MapsUtils.cancelScheduledMapDataUpdateTask(activity)
//                    LoginActivity.start(activity)
//                    activity.finish()
//                }
//
//                true
//            }
//
//            else -> false
//        }
    }

    @CallSuper
    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) : Boolean {
        return false
    }

    @CallSuper
    open fun onConfigurationChanged(newConfig: Configuration) {
    }

    @CallSuper
    open fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return false
    }

    abstract val allObservables: List<Disposable>

    var doOnSignOutBtnClicked: (()->Unit)? = null


}
//endregion