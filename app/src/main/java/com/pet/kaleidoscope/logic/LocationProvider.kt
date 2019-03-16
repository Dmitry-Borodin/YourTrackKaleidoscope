package com.pet.kaleidoscope.logic

import android.content.Context
import android.content.Intent
import android.location.Location
import com.pet.kaleidoscope.service.LocationService
import kotlinx.coroutines.channels.Channel

/**
 * @author Dmitry Borodin on 3/14/19.
 */
class LocationProvider(private val appContext: Context) {


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
}