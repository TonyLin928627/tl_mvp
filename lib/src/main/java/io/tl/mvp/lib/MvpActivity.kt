package io.tl.mvp.lib

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import javax.inject.Inject

abstract class MvpActivity<T: DataBridge>: AppCompatActivity() {

    @Inject
    @JvmField
    var presenter: MvpPresenter<T>?= null

    abstract var doInject: ()->Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doInject()
        presenter!!.onCreate(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        presenter!!.onCreate(savedInstanceState, persistentState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        presenter!!.onNewIntent(intent)
    }
    override fun onStart() {
        super.onStart()
        presenter!!.onStart()
    }

    override fun onRestart() {
        super.onRestart()
        presenter!!.onRestart()
    }

    override fun onResume() {
        super.onResume()
        presenter!!.onResume()
    }

    override fun onPause() {
        presenter!!.onPause()
        super.onPause()
    }

    override fun onStop() {
        presenter!!.onStop()
        super.onStop()
    }
    override fun onDestroy() {
        presenter!!.onDestroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (!presenter!!.onBackPressed()){
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val hasBeenConsumed = presenter!!.onActivityResult(requestCode, resultCode, data)

        if (!hasBeenConsumed){
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (!presenter!!.onRequestPermissionsResult(requestCode, permissions as Array<String>, grantResults)){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        presenter!!.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        presenter!!.onRestoreInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        presenter!!.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        presenter!!.onConfigurationChanged(newConfig)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (presenter!!.onKeyDown(keyCode, event)){
            true
        }else{
            super.onKeyDown(keyCode, event)
        }
    }

}