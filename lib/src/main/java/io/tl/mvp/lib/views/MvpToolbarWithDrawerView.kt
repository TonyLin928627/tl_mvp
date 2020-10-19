package io.tl.mvp.lib.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.google.android.material.appbar.AppBarLayout
import io.reactivex.Observable
import io.tl.mvp.lib.menu.MenuItemsAdapter
import io.tl.mvp.lib.MvpView
import io.tl.mvp.lib.R2
import io.tl.mvp.lib.menu.MenuItem
import io.tl.mvp.lib.toolbar.ToolbarWithDrawer

//region views with toolbar and drawer
abstract class MvpToolbarWithDrawerView(override val menuItemsAdapter: MenuItemsAdapter, val drawerPosition: DrawerPosition) : MvpView(),ToolbarWithDrawer {

    enum class DrawerPosition {
        LEFT,
        RIGHT,
    }

    override val appBar: AppBarLayout by lazy {
        this.screenContainer.findViewById(R2.id.app_bar)
    }

    override fun getContext() = this.mvpActivity as Context

    @BindView(R2.id.drawer_layout)
    override lateinit var drawerLayout: DrawerLayout

    @BindView(R2.id.close_menu)
    override lateinit var closeDrawerBtn: View

    @BindView(R2.id.drawer_menu_rv)
    override lateinit var itemRv: RecyclerView

    @BindView(R2.id.app_info)
    override lateinit var appInfoTv: TextView

    @BindView(R2.id.sign_out_tv)
    override lateinit var signOutTv: TextView

    @BindView(R2.id.actionbar_start)
    override lateinit var toolbarStartButton: ImageView

    @BindView(R2.id.actionbar_centre)
    override lateinit var toolbarCenterView: View

    @BindView(R2.id.actionbar_end)
    override lateinit var toolbarEndButton: ImageView

    override val screenContainerLayoutId = R2.layout.screen_layout_toolbar_with_right_drawer

    override lateinit var onMenuItemClicked: Observable<MenuItem>

    @SuppressLint("MissingSuperCall")
    override fun initViews(){
        inflateView(this, contentLayoutId)
        initDrawer(activity = mvpActivity!!, menuItemsAdapter =  menuItemsAdapter)
    }

}