package com.pet.kaleidoscope.di

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pet.kaleidoscope.logic.FlickrAuthenticator
import com.pet.kaleidoscope.logic.FlickrProvider
import com.pet.kaleidoscope.logic.LocationProvider
import com.pet.kaleidoscope.logic.storage.FlickrRepository
import com.pet.kaleidoscope.logic.storage.FlickrRepositoryImpl
import org.koin.dsl.module

/**
 * @author Dmitry Borodin on 3/16/19.
 */
val appModule = module {

    single<FlickrRepository> { FlickrRepositoryImpl(appContext = get()) }
    single { FlickrAuthenticator(repository = get()) }
    single { FlickrProvider(repository = get()) }

    single<FusedLocationProviderClient> { LocationServices.getFusedLocationProviderClient(get()) }
    single { LocationProvider(appContext = get(), fusedLocationProviderClient = get())  }

}