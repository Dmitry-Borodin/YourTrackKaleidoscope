package com.pet.kaleidoscope

import android.app.Application
import com.flickr4java.flickr.Flickr
import com.pet.kaleidoscope.data.FlickrAuthenticator
import com.pet.kaleidoscope.data.FlickrProvider
import com.pet.kaleidoscope.data.storage.FlickrRepositoryImpl
import com.pet.kaleidoscope.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
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
        startKoin {
            androidContext(this@App)
            androidLogger(if (BuildConfig.DEBUG) Level.INFO else Level.ERROR)
            modules(appModule)
        }
    }

    companion object {
        @Volatile
        lateinit var instance: App
    }
}