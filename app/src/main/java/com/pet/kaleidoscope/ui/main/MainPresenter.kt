package com.pet.kaleidoscope.ui.main

import com.pet.kaleidoscope.data.FlickrProvider
import com.pet.kaleidoscope.ui.base.ScopedPresenter
import kotlinx.coroutines.*

/**
 * @author Dmitry Borodin on 2/22/19.
 */
class MainPresenter : ScopedPresenter() {

    val flickrProvider = FlickrProvider

    var view: MainView? = null

    fun start(view: MainView) {
        this.view = view
    }

    fun stop() {
        this.view = null
    }

    fun onStartStopBottonClicked() = launch(Dispatchers.Main) {

        val isAuthenticated = flickrProvider.getPermissions()

        val url = async(Dispatchers.IO) { flickrProvider.getFlickrPicUrl() }
        view?.showPictures(listOf(url.await()))
    }

    fun onAuthRequestedSuccessfully() {

    }
}