package com.pet.kaleidoscope.logic

import android.content.Context
import android.content.Intent
import android.location.Location
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.pet.kaleidoscope.service.LocationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext

/**
 * @author Dmitry Borodin on 3/14/19.
 */
class LocationProvider(private val appContext: Context,
                       private val fusedLocationProviderClient: FusedLocationProviderClient) { //supporting gms only since it's a pet project

    private val locationRequest: LocationRequest = createLocationRequest()
    val locationSettiongs
    var isTrackingInProgress = false
    var resultChannel: Channel<Location> = Channel()

    fun startLocationTracking(): Channel<Location> {
        startForegroundService()
        return resultChannel
    }

    fun stopLocationTracking() {
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
            smallestDisplacement = 5f //5 Meters
        }
    }

    suspend fun isGpsUsable() : Boolean = withContext(Dispatchers.IO) {
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()
        val client: SettingsClient = LocationServices.getSettingsClient(appContext)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(settingsRequest)
        return@withContext task.result?.locationSettingsStates?.isGpsUsable == true

    }
}