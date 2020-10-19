package io.tl.mvp.lib.toolbar

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.ButterKnife
import com.google.android.material.appbar.AppBarLayout
import io.reactivex.Observable
import io.tl.mvp.lib.MvpView
import io.tl.mvp.lib.R
import io.tl.mvp.lib.clickThrottle

interface Toolbar{
    val appBar: AppBarLayout

    var toolbarStartButton: ImageView


    var toolbarEndButton: ImageView

    var toolbarCenterView: View

    fun toolbarStartBtnObs(): Observable<Unit> = toolbarStartButton.clickThrottle()

    fun toolbarEndBtnObs(): (Observable<Unit>)? = toolbarEndButton.clickThrottle()

    val toolbarTitleStringResId: Int

    val screenContainerLayoutId: Int

    val toolbarContainerLayoutId: Int

    fun getContext(): Context

    fun inflateView(mvpView: MvpView, contentLayoutId: Int){
        mvpView.screenContainer = LayoutInflater.from(getContext()).inflate(screenContainerLayoutId, null) as ViewGroup

        val contentContainer = mvpView.screenContainer.findViewById<ViewGroup>(R.id.content_container)
        View.inflate(getContext(), contentLayoutId, contentContainer)

        val toolbarContainer = mvpView.screenContainer.findViewById<ViewGroup>(R.id.toolbar_container)
        View.inflate(getContext(), toolbarContainerLayoutId, toolbarContainer)

        ButterKnife.bind(this, mvpView.screenContainer)
    }

    fun initToolbar(resources: Resources) {
        if (toolbarCenterView is TextView) {
            toolbarTitleStringResId.takeIf { it > 0 }?.let { theToolbarTitleResId->
                setToolbarTitleText(theToolbarTitleResId)
            }
        }
    }

    fun setToolbarTitleText(stringId: Int){
        if (toolbarCenterView is TextView) {
            (toolbarCenterView as TextView).setText(stringId)
        }
    }

    fun setToolbarTitleText(titleTxt: String){
        if (toolbarCenterView is TextView) {
            (toolbarCenterView as TextView).text = titleTxt
        }
    }
}