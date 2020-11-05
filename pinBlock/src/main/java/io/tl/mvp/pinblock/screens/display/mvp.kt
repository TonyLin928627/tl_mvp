package io.tl.mvp.pinblock.screens.display


import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.tl.mvp.lib.*
import io.tl.mvp.pinblock.R
import io.tl.mvp.pinblock.application.PinBlockDataBridge

//region model
class DisplayModel(dataBridge: PinBlockDataBridge): MvpModel<PinBlockDataBridge>(dataBridge) {

    private fun byteArrayToStringList(bytes: ByteArray): List<String> {

        val hexStrList =  mutableListOf<String>();

        bytes.forEach { b->
            hexStrList.add(String.format("[%02X]", b))
        }

        return hexStrList
    }

    fun generateTextItem(pinBytes: ByteArray, panBytes: ByteArray, pinBlockBytes: ByteArray) : Single<List<String>>{
        return Single.create { emitter->
            val stringList = mutableListOf<String>()

            for (i in 0..8){
                when(i){
                    0 -> stringList.add("")
                    else->stringList.add("${i-1}")
                }
            }

            stringList.add("PIN")
            stringList.addAll(byteArrayToStringList(pinBytes))

            stringList.add("PAN")
            stringList.addAll(byteArrayToStringList(panBytes))

            stringList.add("P.B.")
            stringList.addAll(byteArrayToStringList(pinBlockBytes))

            emitter.onSuccess(stringList)
        }
    }
}

//endregion

//region view
class DisplayView : MvpView(){
    override val contentLayoutId = R.layout.screen_display

    @BindView(R.id.displayRv)
    lateinit var displayRv: RecyclerView

    @BindView(R.id.backBtn)
    lateinit var backBtn: Button

    val onBackBtnClicked by lazy { backBtn.clickThrottle() }

    private val displayAdapter: DisplayAdapter  =  DisplayAdapter()

    override fun initViews() {
        super.initViews()

        ButterKnife.bind(this, screenContainer)

        displayRv.layoutManager = GridLayoutManager(getContext(), 9)

        displayRv.adapter = displayAdapter
    }

    fun displayPinBlockComputation(data: List<String>){
        displayAdapter.items = data
        displayAdapter.notifyDataSetChanged()
    }

    inner class DisplayAdapter : RecyclerView.Adapter<DisplayViewHolder>() {

        var items: List<String> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayViewHolder {
            return DisplayViewHolder(TextView(parent.context))
        }

        override fun onBindViewHolder(holder: DisplayViewHolder, position: Int) {
            (holder as DisplayViewHolder).textView.let {
                it.text = items[position]

                if (position < 9){
                    it.setBackgroundColor(Color.BLUE)
                    it.setTextColor(Color.WHITE)
                }else if (position == 9 || position == 18 || position == 27) {
                    it.setBackgroundColor(Color.BLACK)
                    it.setTextColor(Color.WHITE)
                }else if (position % 2 == 0){
                    it.setBackgroundColor(Color.CYAN)
                }else{
                    it.setBackgroundColor(Color.GREEN)
                }

            }
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

    inner class DisplayViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView) {
        init {
            textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

        }
    }
}
//endregion

//region presenter
class DisplayPresenter(override val mvpView: DisplayView,
                       override val mvpModel: DisplayModel,
                       override val activity: DisplayActivity
) : MvpPresenter<PinBlockDataBridge>(){

    override val allObservables by lazy {
        listOf<Disposable>(
            mvpView.onBackBtnClicked.subscribe{activity.finish()},
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //read pin, pan and pin block data from the previous screen.
       activity.intent?.let { intent ->

           val pinBytes = intent.getByteArrayExtra(DisplayActivity.EXTRA_PIN_BYTES)
           val panBytes = intent.getByteArrayExtra(DisplayActivity.EXTRA_PAN_BYTES)
           val pinBlockBytes = intent.getByteArrayExtra(DisplayActivity.EXTRA_PIN_BLOCK_BYTES)

           //Validate and populate data
           if (pinBytes == null || panBytes == null || pinBlockBytes == null){
               mvpView.showErrorMsg(R.string.invalid_data, R.string.ok){
                   activity.finish()
               }
           }else{
               mvpModel.generateTextItem(pinBytes, panBytes, pinBlockBytes).subscribe { items->
                   mvpView.displayPinBlockComputation(items)
               }.let { addDisposable(it) }
           }

       }
    }

}
//endregion