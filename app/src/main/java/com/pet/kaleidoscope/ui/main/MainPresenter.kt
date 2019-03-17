package com.pet.kaleidoscope.ui.main

import android.Manifest
import com.markodevcic.peko.Peko
import com.pet.kaleidoscope.logic.FlickrAuthenticator
import com.pet.kaleidoscope.logic.FlickrProvider
import com.pet.kaleidoscope.logic.LocationProvider
import com.pet.kaleidoscope.ui.base.ScopedPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import timber.log.Timber

/**
 * @author Dmitry Borodin on 2/22/19.
 */
class MainPresenter : ScopedPresenter() {

    private val flickrProvider: FlickrProvider by GlobalContext.get().koin.inject()
    private val flickrAuth: FlickrAuthenticator by GlobalContext.get().koin.inject()
    private val locationProvider: LocationProvider by GlobalContext.get().koin.inject()

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

        //check permission
        val grantedPermissions = if (Peko.isRequestInProgress()) {
            Peko.resumeRequest()
        } else {
            Peko.requestPermissionsAsync(view!!.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
        }
        Timber.d("result of permission request $grantedPermissions")
        if (Manifest.permission.ACCESS_FINE_LOCATION !in grantedPermissions.grantedPermissions) {
            //TODO show dialog we need it
            return@launch
        }

        //check GPS enabled
        val isGPSUsable = locationProvider.isGpsUsable()
        if (isGPSUsable) {
            //TODO show dialog enable GPS
        }

        val url = flickrProvider.getFlickrPicUrl()
        view?.showPictures(listOf(url))
    }

    fun requestAuthentication() = launch(Dispatchers.Main) {
        //checkPermissions, if not - request it
        val isAuthenticated = flickrAuth.hasReadPermissions() //?: view.showError no network?

        if (isAuthenticated != true) {
            startAuthFlow()
            return@launch
        }
    }

    private suspend fun startAuthFlow() {
        val url = flickrAuth.getAuthUrl()
        view?.requestAuth(url)
    }

    fun onAuthRequestedSuccessfully(verification: String) = launch(Dispatchers.Main) {
        flickrAuth.getAuthToken(verification)
    }
}