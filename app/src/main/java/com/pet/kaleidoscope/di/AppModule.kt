package com.pet.kaleidoscope.di

import com.pet.kaleidoscope.logic.FlickrAuthenticator
import com.pet.kaleidoscope.logic.FlickrProvider
import com.pet.kaleidoscope.logic.storage.FlickrRepository
import com.pet.kaleidoscope.logic.storage.FlickrRepositoryImpl
import org.koin.dsl.module

/**
 * @author Dmitry Borodin on 3/16/19.
 */
val appModule = module {

    single<FlickrRepository> { FlickrRepositoryImpl(get()) }
    single { FlickrAuthenticator(get()) }
    single { FlickrProvider(get()) }

}