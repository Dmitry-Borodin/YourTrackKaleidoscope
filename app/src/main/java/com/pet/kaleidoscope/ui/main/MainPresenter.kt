package com.pet.kaleidoscope.ui.main

import com.pet.kaleidoscope.App
import com.pet.kaleidoscope.data.FlickrProvider
import com.pet.kaleidoscope.ui.base.ScopedPresenter
import kotlinx.coroutines.*

/**
 * @author Dmitry Borodin on 2/22/19.
 */
class MainPresenter : ScopedPresenter() {

    val flickrProvider = App.instance.flickrProvider

    var view: MainView? = null

    fun onAttach(view: MainView) {
        super.onAttach()
        this.view = view
    }

    override fun onDetouch() {
        super.onDetouch()
        this.view = null
    }

    fun onStartStopButtonClicked() = launch(Dispatchers.Main) {

        //checkPermissions, if not - request it

        val isAuthenticated = flickrProvider.hasReadPermissions() //?: view.showError no network?

        if (isAuthenticated != true) {
            view?.requestAuth()
            return@launch
        }

        val url = async(Dispatchers.IO) { flickrProvider.getFlickrPicUrl() }
        view?.showPictures(listOf(url.await()))
    }

    fun onAuthRequestedSuccessfully() {

    }
}