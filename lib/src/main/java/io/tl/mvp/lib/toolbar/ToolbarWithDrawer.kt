package io.tl.mvp.lib.toolbar

import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import io.tl.mvp.lib.menu.MenuItem
import io.tl.mvp.lib.menu.MenuItemsAdapter
import io.tl.mvp.lib.MvpActivity
import io.tl.mvp.lib.clickThrottle


interface ToolbarWithDrawer: Toolbar {


    var drawerLayout: DrawerLayout

    var closeDrawerBtn: View

    var itemRv: RecyclerView

    var appInfoTv: TextView

    var signOutTv: TextView

    fun openDrawer(){
        if (!drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.openDrawer(GravityCompat.END)
        }
    }

    fun closeDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END)
        }
    }

    fun onSignOutBtnClicked() = signOutTv.clickThrottle()

    var onMenuItemClicked: Observable<MenuItem>

    fun initDrawer(activity: MvpActivity, menuItemsAdapter: MenuItemsAdapter) {

        onMenuItemClicked = menuItemsAdapter.clickSubject.doOnNext {closeDrawer()}

        closeDrawerBtn.setOnClickListener(){
            closeDrawer()
        }

        itemRv.layoutManager = LinearLayoutManager(activity)
        itemRv.adapter = menuItemsAdapter

//        with(activity.resources){
//
//            appInfoTv.text = (getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME + " " + "\n" + getString(R.string.version) + ": " + BuildConfig.VERSION_CODE + " " + BuildConfig.FLAVOR)
//        }


    }

    val menuItemsAdapter: MenuItemsAdapter

    fun populateMenuItems(getMenuItems: ()-> Single<List<MenuItem>>) {

        getMenuItems().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : DisposableSingleObserver<List<MenuItem>>(){
                    override fun onSuccess(menuItems: List<MenuItem>) {
                        dispose()

                        with(menuItemsAdapter.mMenuItemList){
                            clear()
                            addAll(menuItems)
                        }

                        menuItemsAdapter.notifyDataSetChanged()
                    }

                    override fun onError(e: Throwable) {

                        dispose()
                    }

                }
            )
    }

}