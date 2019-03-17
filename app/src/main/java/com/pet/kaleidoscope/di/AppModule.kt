package com.pet.kaleidoscope.di

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pet.kaleidoscope.logic.FlickrAuthenticator
import com.pet.kaleidoscope.logic.FlickrUrlProvider
import com.pet.kaleidoscope.logic.LocationProvider
import com.pet.kaleidoscope.logic.storage.FlickrRepository
import com.pet.kaleidoscope.logic.storage.FlickrRepositoryImpl
import com.pet.kaleidoscope.ui.main.MainActivity
import com.pet.kaleidoscope.ui.main.MainPresenter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * @author Dmitry Borodin on 3/16/19.
 */
val appModule = module {

    single<FlickrRepository> { FlickrRepositoryImpl(appContext = get()) }
    single { FlickrAuthenticator(repository = get()) }
    single { FlickrUrlProvider(repository = get()) }

    scoped<FusedLocationProviderClient> { LocationServices.getFusedLocationProviderClient(androidContext()) }
    scoped { LocationProvider(appContext = get(), fusedLocationProviderClient = get(), flickrUrlProvider = get()) }

    scope<MainActivity> {
        scoped { MainPresenter(flickrAuth = get(), locationProvider = get()) }
    }
}