package com.pet.kaleidoscope

import android.app.Application
import com.flickr4java.flickr.Flickr
import com.pet.kaleidoscope.data.FlickrAuthenticator
import com.pet.kaleidoscope.data.FlickrProvider
import com.pet.kaleidoscope.data.PreferencesRepository
import timber.log.Timber

/**
 * @author Dmitry Borodin on 3/10/19.
 */
class App : Application() {

    val flickrProvider = FlickrProvider(PreferencesRepository())
    val flickrAuthenticator = FlickrAuthenticator(PreferencesRepository())

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