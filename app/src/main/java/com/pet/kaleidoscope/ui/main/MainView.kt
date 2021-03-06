package com.pet.kaleidoscope.ui.main

import android.app.Activity
import com.pet.kaleidoscope.models.TrackingPoint

/**
 * @author Dmitry Borodin on 2/22/19.
 */
interface MainView {
    fun setStateStopped()
    fun setStateRunning()
    fun showPictures(urls: List<TrackingPoint>)
    fun requestAuth(url: String)
    fun getActivity() : Activity
    fun showInformationDialog(text: String)
}