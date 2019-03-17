package com.pet.kaleidoscope.logic

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.pet.kaleidoscope.Constants
import com.pet.kaleidoscope.service.LocationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Dmitry Borodin on 3/14/19.
 */
class LocationProvider(
    private val appContext: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient //supporting gms only since it's a pet project
) {

    private val locationRequest: LocationRequest = createLocationRequest()
    private val locationUpdateCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?): Unit = GlobalScope.launch {
            resultChannel.send(locationResult?.lastLocation)
        }.ignore()
    }
    var isTrackingInProgress = false
    var resultChannel: Channel<Location?> = Channel()

    private fun Any.ignore(): Unit = Unit

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    fun startLocationTracking(): Channel<Location?> {
        startForegroundService()
        isTrackingInProgress = true
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationUpdateCallback,
            Looper.getMainLooper()
        )
        return resultChannel
    }

    fun stopLocationTracking() {
        isTrackingInProgress = false
        fusedLocationProviderClient.removeLocationUpdates(locationUpdateCallback)
        stopForegroundService()
    }

    private fun startForegroundService() {
        val intent = Intent(appContext, LocationService::class.java).apply {
            action = LocationService.START_TRACKING
        }
        appContext.startService(intent)
    }

    private fun stopForegroundService() {
        val intent = Intent(appContext, LocationService::class.java).apply {
            action = LocationService.STOP_TRACKING
        }
        appContext.startService(intent)
    }


    private fun createLocationRequest(): LocationRequest {
        return LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 1000 * 5
            smallestDisplacement = Constants.MINIMIM_DISTANCE_THRESHOLD_IN_METERS.toFloat()
        }
    }

    suspend fun isGpsUsable(): Boolean = withContext(Dispatchers.IO) {
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()
        val client: SettingsClient = LocationServices.getSettingsClient(appContext)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(settingsRequest)
        return@withContext task.result?.locationSettingsStates?.isGpsUsable == true
    }
}