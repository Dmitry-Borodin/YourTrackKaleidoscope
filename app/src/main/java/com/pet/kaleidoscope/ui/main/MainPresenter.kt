package com.pet.kaleidoscope.ui.main

import android.Manifest
import com.markodevcic.peko.Peko
import com.pet.kaleidoscope.R
import com.pet.kaleidoscope.logic.FlickrAuthenticator
import com.pet.kaleidoscope.logic.FlickrUrlProvider
import com.pet.kaleidoscope.logic.LocationProvider
import com.pet.kaleidoscope.models.TrackingPoint
import com.pet.kaleidoscope.ui.base.ScopedPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.last
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author Dmitry Borodin on 2/22/19.
 */
class MainPresenter(
    private val flickrAuth: FlickrAuthenticator,
    private val locationProvider: LocationProvider
) : ScopedPresenter() {

    private var view: MainView? = null
    private var gpsRequested = false

    fun onAttach(view: MainView) {
        super.onAttach()
        this.view = view
        if (locationProvider.isTrackingInProgress) {
            view.setStateRunning()
            launch {
                locationProvider.repeatLast()
                for (points in locationProvider.resultChannel) {
                    showPointOnScreen(points)
                }
            }
        } else {
            view.setStateStopped()
        }
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
            if (!isGPSUsable && !gpsRequested) {
                view?.showInformationDialog(view!!.getActivity().getString(R.string.gps_not_usable_message))
                gpsRequested = true
                Timber.d("GPS not usable")
            }

            view?.setStateRunning()
            val channel = locationProvider.startLocationTracking()
            for (points in channel) {
                showPointOnScreen(points)
            }
        }
    }

    private fun showPointOnScreen(points: List<TrackingPoint>) {
        val urlList = points.asSequence().distinctBy { it.url }.map { it.url }.toList()
        view?.showPictures(urlList)
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