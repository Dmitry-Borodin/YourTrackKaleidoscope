package com.pet.kaleidoscope.di

import com.pet.kaleidoscope.data.FlickrAuthenticator
import com.pet.kaleidoscope.data.FlickrProvider
import com.pet.kaleidoscope.data.storage.FlickrRepository
import com.pet.kaleidoscope.data.storage.FlickrRepositoryImpl
import org.koin.dsl.module

/**
 * @author Dmitry Borodin on 3/16/19.
 */
val appModule = module {

    single<FlickrRepository> { FlickrRepositoryImpl(get()) }
    single { FlickrAuthenticator(get()) }
    single { FlickrProvider(get()) }

}