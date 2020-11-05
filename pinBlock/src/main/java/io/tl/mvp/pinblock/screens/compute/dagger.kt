package io.tl.mvp.pinblock.screens.compute

import dagger.Component
import dagger.Module
import dagger.Provides
import io.tl.mvp.lib.MvpPresenter
import io.tl.mvp.pinblock.application.AppComponent
import io.tl.mvp.pinblock.application.PinBlockDataBridge
import io.tl.mvp.pinblock.screens.compute.mvp.ComputeModel
import io.tl.mvp.pinblock.screens.compute.mvp.ComputePresenter
import io.tl.mvp.pinblock.screens.compute.mvp.ComputeView
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class ComputeScope

@ComputeScope
@Component(modules = [ComputeModule::class], dependencies = [AppComponent::class])
interface MainComponent {
    fun inject(activity: ComputeActivity)
}

@Module
class ComputeModule(private val activity: ComputeActivity) {
    @Provides
    @ComputeScope
    fun presenter(view: ComputeView, model: ComputeModel): MvpPresenter<PinBlockDataBridge> {
        return ComputePresenter(view, model, activity)
    }

    @Provides
    @ComputeScope
    fun view(): ComputeView {
        return ComputeView()
    }

    @Provides
    @ComputeScope
    fun model(dataBridge: PinBlockDataBridge): ComputeModel {
        return ComputeModel(dataBridge)
    }
}