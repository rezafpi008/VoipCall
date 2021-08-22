package com.example.materialdesignvoipcall.util

import android.app.Application
import android.content.Context

class MainApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        /*@SuppressLint("StaticFieldLeak")
        lateinit var coreContext: CoreContext

        fun ensureCoreExists(context: Context, coreListener: CoreListenerStub) {
            coreContext = CoreContext(context)
            coreContext.startLogin(coreListener)
        }*/
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = MainApplication.applicationContext()
    }
}