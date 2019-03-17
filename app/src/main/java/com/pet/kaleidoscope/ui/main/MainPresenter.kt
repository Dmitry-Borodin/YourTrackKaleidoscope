package com.pet.kaleidoscope.ui.main

import android.Manifest
import com.markodevcic.peko.Peko
import com.pet.kaleidoscope.R
import com.pet.kaleidoscope.logic.FlickrAuthenticator
import com.pet.kaleidoscope.logic.FlickrUrlProvider
import com.pet.kaleidoscope.logic.LocationProvider
import com.pet.kaleidoscope.ui.base.ScopedPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author Dmitry Borodin on 2/22/19.
 */
class MainPresenter(
    private val flickrUrlProvider: FlickrUrlProvider,
    private val flickrAuth: FlickrAuthenticator,
    private val locationProvider: LocationProvider
) : ScopedPresenter() {

    var view: MainView? = null

    fun onAttach(view: MainView) {
        super.onAttach()
        this.view = view
        //TODO check is tracking in progress
    }

    override fun onDetouch() {
        super.onDetouch()
        this.view = null
    }

    fun onStartStopButtonClicked() = launch(Dispatchers.Main) {

        if (locationProvider.isTrackingInProgress) {
            locationProvider.stopLocationTracking()
            view?.setStateStopped()
        } else {
            //check permission
            val grantedPermissions = if (Peko.isRequestInProgress()) {
                Peko.resumeRequest()
            } else {
                Peko.requestPermissionsAsync(view!!.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            }
            Timber.d("result of permission request $grantedPermissions")
            if (Manifest.permission.ACCESS_FINE_LOCATION !in grantedPermissions.grantedPermissions) {
                view?.showInformationDialog(view!!.getActivity().getString(R.string.location_permission_missing_message))
                Timber.d("Don't have location permission")
                return@launch
            }

            //check GPS enabled
            val isGPSUsable = locationProvider.isGpsUsable()
            if (!isGPSUsable) {
                view?.showInformationDialog(view!!.getActivity().getString(R.string.gps_not_usable_message))
                Timber.d("GPS not usable")
                return@launch
            }

            val channel = locationProvider.startLocationTracking()
            for (point in channel) {
                view?.showPictures(listOf(point.url))
            }
        }
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