package com.pet.kaleidoscope

import android.app.Application
import com.flickr4java.flickr.Flickr
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
            Flickr.debugRequest = true
        }
    }

    companion object {
        @Volatile
        lateinit var instance: App
    }
}