package com.jamesnyakush.appcentral

import android.app.Application
import timber.log.Timber

class AppCentral : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return super.createStackElementTag(element) + ":" + element.lineNumber
                }
            })
        } else {
           // Timber.plant(CrashlyticsTree())
        }
    }
}