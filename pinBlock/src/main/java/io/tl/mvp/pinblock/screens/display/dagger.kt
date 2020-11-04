package io.tl.mvp.pinblock.screens.display

import dagger.Component
import dagger.Module
import dagger.Provides
import io.tl.mvp.lib.MvpPresenter
import io.tl.mvp.pinblock.application.AppComponent
import io.tl.mvp.pinblock.application.PinBlockDataBridge
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class DisplayScope

@DisplayScope
@Component(modules = [DisplayModule::class], dependencies = [AppComponent::class])
interface DisplayComponent {
    fun inject(activity: DisplayActivity)
}

@Module
class DisplayModule(private val activity: DisplayActivity) {
    @Provides
    @DisplayScope
    fun presenter(view: DisplayView, model: DisplayModel): MvpPresenter<PinBlockDataBridge> {
        return DisplayPresenter(view, model, activity)
    }

    @Provides
    @DisplayScope
    fun view(): DisplayView {
        return DisplayView()
    }

    @Provides
    @DisplayScope
    fun model(dataBridge: PinBlockDataBridge): DisplayModel {
        return DisplayModel(dataBridge)
    }
}