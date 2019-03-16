package com.pet.kaleidoscope.ui.main

import android.app.Activity

/**
 * @author Dmitry Borodin on 2/22/19.
 */
interface MainView {
    fun setStateStopped()
    fun setStateRunning()
    fun showPictures(urls: List<String?>)
    fun requestAuth(url: String)
    fun getActivity() : Activity
}