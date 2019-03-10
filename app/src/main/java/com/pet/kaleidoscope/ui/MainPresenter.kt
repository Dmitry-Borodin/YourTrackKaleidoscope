package com.pet.kaleidoscope.ui

import com.pet.kaleidoscope.data.TrackProvider
import kotlinx.coroutines.*

/**
 * @author Dmitry Borodin on 2/22/19.
 */
class MainPresenter() {

    var view: MainView? = null

    fun start(view: MainView) {
        this.view = view
    }

    fun stop() {
        this.view = null
    }

    fun onStartStopBottonClicked() = GlobalScope.launch(Dispatchers.Main) {
        val url = async(Dispatchers.IO) { TrackProvider.getFlickrPicUrl() }
        view?.showPictures(listOf(url.await()))
    }
}