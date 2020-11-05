package io.tl.mvp.lib


import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.CallSuper
import butterknife.ButterKnife
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.TimeUnit

//region base view definitions
abstract class MvpView{

    private lateinit var weakContext : WeakReference<(Context)>

    public fun getContext(): Context{
        return weakContext.get()!!
    }
    
    private val disposables = CompositeDisposable()

    protected fun addDisposable(vararg d: Disposable){
        d.forEach {
            disposables.add(it)
        }

    }
    lateinit var screenContainer: ViewGroup

    protected abstract val  contentLayoutId: Int

    @CallSuper
    protected open fun initViews(){
        screenContainer = FrameLayout(getContext())
        View.inflate(getContext(), contentLayoutId, screenContainer)

        ButterKnife.bind(this, screenContainer)
    }

    internal fun doDeinit(){
        disposables.clear()
    }

    fun doInit(getContext : (()->Context)) {
        weakContext = WeakReference(getContext.invoke())
        //Inflate the layout into the viewGroup
        initViews()

    }

    private var progressDialog: AlertDialog? = null
    private var progressBar: ProgressBar? = null
    private fun hideAllDialogs(){
        hideLoading()
        hideDialog()
        hideProgress()
    }

    //region loading hub
    //a loading hub.  which cannot be dismissed/closed/hidden by the user
    private var loadingDialog: ProgressDialog? = null

    fun showLoading( title:String? = null, msg: String? ="Please wait..."){
        screenContainer.post{
            hideDialog()
            hideProgress()

            if(loadingDialog==null) {
                loadingDialog = ProgressDialog.show(getContext(), title, msg, true)
            }
        }

    }

    fun hideLoading() {
        screenContainer.post{
            Log.d("hideOverlays", "hideLoadingDialog $loadingDialog")
            loadingDialog?.let {
                it.dismiss()
                loadingDialog = null
            }
        }
    }
    //endregion

    //region dialog
    private var dialog: AlertDialog? = null
    protected fun hideDialog(){
        screenContainer.post {
            Log.d("hideOverlays", "hideDialog $dialog")
            dialog?.dismiss()
        }
    }

    /**
     * show a dialog with "Okay" and "Cancel" buttons
     */
    fun showAlertMsg( alertTitle:String, alertMsg: String?=null, onButtonClicked: ((yesOrNo: Boolean)->Unit)? = null){
        hideAllDialogs()

        screenContainer.post {
            dialog = AlertDialog.Builder(getContext())
                .setTitle(alertTitle)
                .setMessage(alertMsg)
                .setCancelable(true)
                .setPositiveButton(R.string.ok) { _, _ -> onButtonClicked?.invoke(true) }
                .setNegativeButton(R.string.cancel) { _, _ -> onButtonClicked?.invoke(false) }
                .setOnDismissListener { dialog = null }
                .show()
        }
    }

    /**
     * show a dialog with "Yes" and "No" buttons
     */
    fun showWarningMsg( warningTitle: String, warningMsg: String, onYesOrNoClicked: ((yesOrNo: Boolean)->Unit)? = null){
        hideAllDialogs()

        screenContainer.post {
            dialog = AlertDialog.Builder(getContext())
                .setTitle(warningTitle)
                .setMessage(warningMsg)
                .setCancelable(true)
                .setPositiveButton(R.string.yes) { _, _ -> onYesOrNoClicked?.invoke(true) }
                .setNegativeButton(R.string.no) { _, _ -> onYesOrNoClicked?.invoke(false) }
                .setOnDismissListener { dialog = null }
                .show()
        }
    }

    fun showWarningMsg( warningMsg: String, onYesOrNoClicked: ((yesOrNo: Boolean)->Unit)? = null){
        hideAllDialogs()

        screenContainer.post {
            dialog = AlertDialog.Builder(getContext())
                .setTitle("WARNING")
                .setMessage(warningMsg)
                .setCancelable(true)
                .setPositiveButton(R.string.yes) { _, _ -> onYesOrNoClicked?.invoke(true) }
                .setNegativeButton(R.string.no) { _, _ -> onYesOrNoClicked?.invoke(false) }
                .setOnDismissListener { dialog = null }
                .show()
        }
    }

    fun getErrMsgFromThrowable(throwable: Throwable) : String {
        var errorMessage = throwable.localizedMessage?.takeIf { it.isNotBlank() } ?: throwable.toString()
        
        return errorMessage
    }

    /**
     * Show a dialog with a "Okay" button
     */
    fun showErrorMsg( throwable: Throwable, onBtnClicked: (()->Unit)? = null ){

        screenContainer.post {
            val errTitle = ""
            val errMsg = getErrMsgFromThrowable(throwable)
            val btnMsgId = R.string.ok
            val cancelable = true

            val action =  onBtnClicked

            showErrorMsgWithTitle(errTitle, errMsg = errMsg, btnMsgId = btnMsgId, cancelable = cancelable, onBtnClicked = action)
        }
    }

    /**
     * Show a dialog with a "Okay" button
     */
    fun showErrorMsg( msg: String?, throwable: Throwable, onBtnClicked: (()->Unit)? = null ){

        screenContainer.post {
            val errTitle = ""
            val errMsg = msg ?: getErrMsgFromThrowable(throwable)
            val btnMsgId = R.string.ok
            val cancelable = true
            val action =    onBtnClicked

            showErrorMsgWithTitle(errTitle, errMsg = errMsg, btnMsgId = btnMsgId, cancelable = cancelable, onBtnClicked = action)
        }
    }

    /**
     * Show a dialog with a  button
     */
    fun showErrorMsg(  errMsgResId: Int, btnMsgId: Int, onBtnClicked: (()->Unit)? = null ){

        screenContainer.post {
            showErrorMsgWithTitle(errTitle="", errMsgId = errMsgResId, btnMsgId = btnMsgId, onBtnClicked = onBtnClicked)
        }
    }

    fun showErrorMsgWithTitle( errTitleId: Int, errMsg: String?=null, btnMsgId: Int = R.string.ok, onBtnClicked: (()->Unit)? = null ){
        hideAllDialogs()

        screenContainer.post {
            dialog = AlertDialog.Builder(getContext())
                .setTitle(errTitleId)
                .setMessage(errMsg)
                .setCancelable(true)
                .setNegativeButton(btnMsgId) { _, _ -> onBtnClicked?.invoke() }
                .setOnDismissListener { dialog = null }
                .show()
        }
    }

    fun showMsgWithTitle( errTitleId: Int, errMsgId: Int, btnMsgId: Int, onBtnClicked: (()->Unit)? = null ){

        hideAllDialogs()

        screenContainer.post {
            dialog = AlertDialog.Builder(getContext())
                .setTitle(errTitleId)
                .setMessage(errMsgId)
                .setCancelable(false)
                .setNegativeButton(btnMsgId) { _, _ -> onBtnClicked?.invoke() }
                .setOnDismissListener { dialog = null }
                .show()
        }
    }

    fun showErrorMsgWithTitle( errTitle:String, errMsg: String?=null, btnMsgId: Int = R.string.ok, cancelable: Boolean = true, onBtnClicked: (()->Unit)? = null ){
        hideAllDialogs()

        screenContainer.post {
            dialog = AlertDialog.Builder(getContext())
                .setTitle(errTitle)
                .setMessage(errMsg)
                .setCancelable(cancelable)
                .setNegativeButton(btnMsgId) { _, _ -> onBtnClicked?.invoke() }
                .setOnDismissListener { dialog = null }
                .show()
        }
    }

    fun showErrorMsgWithTitle( errTitle:String, errMsgId: Int, btnMsgId: Int = R.string.ok, onBtnClicked: (()->Unit)? = null ) {
        hideAllDialogs()

        screenContainer.post {
            dialog = AlertDialog.Builder(getContext())
                .setTitle(errTitle)
                .setMessage(errMsgId)
                .setCancelable(true)
                .setNegativeButton(btnMsgId) { _, which -> onBtnClicked?.invoke() }
                .setOnDismissListener { dialog = null }
                .show()
        }
    }

    fun showErrorMsgAndTryAgain( throwable: Throwable, cancelable: Boolean = true,  onTryAgainClicked: (()->Unit)){
        hideAllDialogs()

        screenContainer.post {
            val errMsg = getErrMsgFromThrowable(throwable)

            showErrorMsgWithTitle("", errMsg = errMsg, btnMsgId = R.string.try_again, cancelable = cancelable, onBtnClicked = onTryAgainClicked)
        }
    }
    /**
     * show a dialog with progress bar
     */
    private fun play(from: Int, to: Int, max: Int, duration: Long, completion: (()->Unit)?) {
        progressBar?.let { theProgressBar->
            val weight = duration.toInt()
            theProgressBar.max = max*weight

            ValueAnimator.ofInt(from * weight, to * weight).apply {
                this.duration = duration
                this.repeatCount = 0

                this.addUpdateListener {
                        valueAnimator->

                    theProgressBar.progress = valueAnimator.animatedValue as Int
                }
                this.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        completion?.invoke()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }
                })

                this.start()
            }
        }

    }
    fun showProgress(activity: Activity, from: Int, to: Int, max: Int, duration: Long, completion: (()->Unit)? = null){
        hideLoading()
        hideDialog()

        screenContainer.post {

            if (progressDialog == null){
                activity.window
                val view = LayoutInflater.from(activity).inflate(R.layout.dialog_progress_bar, null)
                progressBar = view as ProgressBar
                progressDialog = AlertDialog.Builder(activity)
                    .setView(view)
                    .setCancelable(false)
                    .setOnDismissListener {
                        progressDialog = null
                        progressBar = null
                    }.show().apply {
                        this.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    }

            }

            play(from, to, max, duration, completion)

        }

    }

    fun hideProgress(){
        screenContainer.post {
            Log.d("hideOverlays", "hideProgressDialog $progressDialog")
            progressDialog?.dismiss()
        }
    }




    //endregion
    fun hideKeyboard(activity: Activity) {
        activity.let{ theActivity->
            theActivity.currentFocus?.let {currentFocus->
                (theActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(currentFocus.windowToken, 0)
            }
        }

    }

    fun showSnackBar(message: String) {
        hideAllDialogs()

        screenContainer.post {
            Snackbar.make(screenContainer, message, Snackbar.LENGTH_LONG).show()
        }
    }

}

@SuppressLint("SetTextI18n")
fun TextView.displayBuildInfo(appName: String, versionName: String, versionCode: Int,  backend: String?, flavor: String? ) {

    val backendStr = backend?.let { it->"_$it".toUpperCase(Locale.ROOT)} ?: ""

    this.text = "${appName}  ${versionName}\n" +
            "${context.getString(R.string.version)} : ${versionCode}\n${flavor?.toUpperCase(Locale.ROOT) ?: ""} ${backendStr}}"
}

fun EditText.capitalizeInputText(textWatcher: TextWatcher? = null){
    this.addTextChangedListener(
        object : TextWatcher{

            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
                Log.d("capitalizeText1", "<$charSequence> $start, $count, $after")

                this@capitalizeInputText.removeTextChangedListener(this)
                textWatcher?.beforeTextChanged(charSequence, start, count, after)
                this@capitalizeInputText.addTextChangedListener(this)

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Log.d("capitalizeText2", "<$s> $start, $before, $count")


                this@capitalizeInputText.removeTextChangedListener(this)
                textWatcher?.onTextChanged(s, start, before, count)
                this@capitalizeInputText.addTextChangedListener(this)

            }

            override fun afterTextChanged(editable: Editable) {
                Log.d("capitalizeText3", "<$editable>")

                this@capitalizeInputText.removeTextChangedListener(this)
                editable.let {
                    val c = it.toString().toUpperCase(locale = Locale.ENGLISH)
                    if (c.isNotBlank()) {
                        editable.replace(0, it.length, c)
                    }
                }
                textWatcher?.afterTextChanged(editable)
                this@capitalizeInputText.addTextChangedListener(this)
            }

        }
    )
}
//endregion

//region view extensions


fun View.clickThrottle(duration: Long = 500): Observable<Unit> {
    return this.clicks().throttleFirst(duration, TimeUnit.MILLISECONDS)
}

fun <T> PublishSubject<T>.throttle(duration: Long = 500): Observable<T> {
    return this.throttleFirst( duration, TimeUnit.MILLISECONDS)
}

fun View.clickThrottleWithFadeInOut(): Observable<Unit> {

    return this.clicks().throttleFirst(500, TimeUnit.MILLISECONDS)
        .doOnNext {
            ValueAnimator.ofFloat(1.0f, 0.5f).apply {
                duration = 450
                repeatCount = 0


                this.addUpdateListener {
                        valueAnimator->

                    this@clickThrottleWithFadeInOut.alpha = valueAnimator.animatedValue as Float
                }

                this.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                        Log.d("WithFadeInOut", "onAnimationRepeat")
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        Log.d("WithFadeInOut", "onAnimationEnd")
                        this@clickThrottleWithFadeInOut.alpha = 1.0f
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        Log.d("WithFadeInOut", "onAnimationCancel")
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        Log.d("WithFadeInOut", "onAnimationStart")
                    }
                })

            }.start()

        }
        .delay(150, TimeUnit.MILLISECONDS)
}
//endregion
