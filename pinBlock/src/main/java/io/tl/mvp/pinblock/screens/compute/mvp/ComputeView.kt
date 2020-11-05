package io.tl.mvp.pinblock.screens.compute.mvp

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.subjects.PublishSubject
import io.tl.mvp.lib.MvpView
import io.tl.mvp.lib.clickThrottle
import io.tl.mvp.pinblock.R

class ComputeView : MvpView(){
    override val contentLayoutId = R.layout.screen_compute

    @BindView(R.id.panTv)
    lateinit var panTv: TextView

    @BindView(R.id.pinEt)
    lateinit var pinEt: EditText

    @BindView(R.id.computeBtn)
    lateinit var submitBtn: Button

    @BindView(R.id.pinLengthTv)
    lateinit var pinLengthTv: TextView

    val onSubmitBtnClicked by lazy { submitBtn.clickThrottle() }

    val onPinReady = PublishSubject.create<Boolean>()

    override fun initViews() {
        super.initViews()

        ButterKnife.bind(this, screenContainer)

        //set text changed listener
        pinEt.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(editable: Editable) {

                    val pinLength = editable.trim().length
                    (pinLength in 4..12).let {isReady-> onPinReady.onNext( isReady ) }

                    pinLengthTv.text = String.format(getContext().getString(R.string.pin_length), pinLength)
                }
            }
        )
    }
}