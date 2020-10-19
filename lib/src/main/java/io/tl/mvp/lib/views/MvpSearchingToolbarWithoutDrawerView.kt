package io.tl.mvp.lib.views

import androidx.appcompat.widget.SearchView
import io.reactivex.subjects.PublishSubject
import io.tl.mvp.lib.toolbar.SearchingToolbar

abstract class MvpSearchingToolbarWithoutDrawerView() : MvpToolbarWithoutDrawerView(),
    SearchingToolbar {

    override val searchEventsPublishSubject = PublishSubject.create<Pair<SearchingToolbar.SearchEvent, Any?>>()

    override lateinit var searchView: SearchView

    override val toolbarTitleStringResId = 0

    override fun initViews() {
        super.initViews()

        toolbarCenterView.takeIf { it is SearchView }?.let { theToolbarCenterView->
            initSearchingToolbar(theToolbarCenterView as SearchView)
        }

    }
}