package co.spaceconnect.visitor

import android.content.Context
import android.widget.TextView

fun TextView.displayBuildInfo(context: Context) {

    this.text = "${BuildConfig.VERSION_NAME}\n" +
            "${context.getString(R.string.version)} : ${BuildConfig.VERSION_CODE}\n${BuildConfig.FLAVOR_brand.toUpperCase()}"+

            BuildConfig.FLAVOR_backend.let { backend->
                when(backend){
                    "prod"-> ""
                    else -> "_$backend".toUpperCase()
                }
            }


}