package com.pet.kaleidoscope

import android.app.Application
import timber.log.Timber

/**
 * @author Dmitry Borodin on 3/10/19.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        @Volatile
        lateinit var instance: App
    }
}