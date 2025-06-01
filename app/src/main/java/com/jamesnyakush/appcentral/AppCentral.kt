package com.jamesnyakush.appcentral

import android.app.Application
import com.jamesnyakush.appcentral.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber

class AppCentral : Application() {

    override fun onCreate() {
        super.onCreate()

        initTimber()
        initKoin()
    }

    /**
     * Initializes Timber for logging.
     * In debug builds, it uses a custom DebugTree that includes line numbers.
     * In release builds, it can be configured to use a CrashlyticsTree or similar.
     */
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

    /**
     * Initializes Koin dependency injection framework.
     * Sets the Android context and loads the app module.
     */
    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@AppCentral)
            modules(appModule)
        }
    }
}