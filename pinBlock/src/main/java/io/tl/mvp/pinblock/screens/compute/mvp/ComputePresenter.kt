package io.tl.mvp.pinblock.screens.compute.mvp

import android.os.Bundle
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.tl.mvp.lib.MvpPresenter
import io.tl.mvp.pinblock.application.PinBlockDataBridge
import io.tl.mvp.pinblock.screens.compute.ComputeActivity
import io.tl.mvp.pinblock.screens.display.DisplayActivity

class ComputePresenter(override val mvpView: ComputeView,
                       override val mvpModel: ComputeModel,
                       override val activity: ComputeActivity
) : MvpPresenter<PinBlockDataBridge>(){

    override val allObservables by lazy {
        listOf<Disposable>(
            mvpView.onSubmitBtnClicked.subscribe{this.doOnSignInBtnClicked()},
            mvpView.onPinReady.subscribe { isReady ->
                mvpModel.setPin(mvpView.pinEt.text.toString()).blockingAwait()
                mvpView.submitBtn.isEnabled = isReady
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init UI
        mvpView.panTv.text = mvpModel.getPan().blockingGet()
        mvpView.pinEt.setText(mvpModel.getPin().blockingGet())
    }

    private fun doOnSignInBtnClicked (){

        mvpView.hideKeyboard(activity)

        mvpModel.generatePinBlockFormat3()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{pinBlockComputation->
            DisplayActivity.start(activity, pinBlockComputation)
        }.let { addDisposable(it) }
    }
}