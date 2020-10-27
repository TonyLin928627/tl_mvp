package io.tl.mvp.lib.views

import android.content.Context
import androidx.appcompat.widget.SearchView
import io.reactivex.subjects.PublishSubject
import io.tl.mvp.lib.menu.MenuItemsAdapter
import io.tl.mvp.lib.toolbar.SearchingToolbar

abstract class MvpSearchingToolbarWithDrawerView(menuItemsAdapter: MenuItemsAdapter, drawerPosition: DrawerPosition) : MvpToolbarWithDrawerView(menuItemsAdapter, drawerPosition),
    SearchingToolbar {


//    override fun getContext(): Context = this.mvpActivity as Context

    override val searchEventsPublishSubject =
        PublishSubject.create<Pair<SearchingToolbar.SearchEvent, Any?>>()
    override lateinit var searchView: SearchView

    override val toolbarTitleStringResId = 0

    override fun initViews() {
        super.initViews()

        toolbarCenterView.takeIf { it is SearchView }?.let { theToolbarCenterView->
            initSearchingToolbar(theToolbarCenterView as SearchView)
        }

    }

}