package co.spaceconnect.visitor

import android.app.Application

class App: Application() {

    companion object {
        val appComponent: AppComponent by lazy {
           DaggerAppComponent.builder().build()
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}