package com.pet.kaleidoscope

import android.app.Application
import com.flickr4java.flickr.Flickr
import com.pet.kaleidoscope.data.FlickrAuthenticator
import com.pet.kaleidoscope.data.FlickrProvider
import com.pet.kaleidoscope.data.storage.FlickrRepositoryImpl
import timber.log.Timber

/**
 * @author Dmitry Borodin on 3/10/19.
 */
class App : Application() {

    val flickrProvider by lazy { FlickrProvider(FlickrRepositoryImpl(this)) }
    val flickrAuthenticator by lazy {  FlickrAuthenticator(FlickrRepositoryImpl(this)) }

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