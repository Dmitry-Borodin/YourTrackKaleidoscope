package com.pet.kaleidoscope.ui

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

    fun onStartStopBottonClicked() {

    }
}