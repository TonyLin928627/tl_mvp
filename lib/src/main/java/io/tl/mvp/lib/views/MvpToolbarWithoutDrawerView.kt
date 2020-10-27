package io.tl.mvp.lib.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import butterknife.BindView
import com.google.android.material.appbar.AppBarLayout
import io.tl.mvp.lib.MvpView
import io.tl.mvp.lib.R
import io.tl.mvp.lib.R2
import io.tl.mvp.lib.toolbar.Toolbar

//region views with toolbar but without drawer
abstract class MvpToolbarWithoutDrawerView :  MvpView(), Toolbar {

    override val appBar: AppBarLayout by lazy {
        this.screenContainer.findViewById<AppBarLayout>(R.id.app_bar)
    }

//    override fun getContext(): Context {
//        return this.mvpActivity as Context
//    }

    @BindView(R2.id.actionbar_start)
    override lateinit var toolbarStartButton: ImageView

    @BindView(R2.id.actionbar_centre)
    override lateinit var toolbarCenterView: View

    @BindView(R2.id.actionbar_end)
    override lateinit var toolbarEndButton: ImageView

    override val screenContainerLayoutId = R.layout.screen_layout_toolbar_without_drawer

    @SuppressLint("MissingSuperCall")
    override fun initViews(){
        inflateView(this, contentLayoutId)
    }
}