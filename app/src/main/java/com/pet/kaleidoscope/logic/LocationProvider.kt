package com.pet.kaleidoscope.logic

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import com.pet.kaleidoscope.service.LocationService
import kotlinx.coroutines.channels.Channel

/**
 * @author Dmitry Borodin on 3/14/19.
 */
class LocationProvider(private val appContext: Context) {

    private val serviceConnection = object ::ServiceConnection() {
        override fun onServiceConnected(className: android.content.ComponentName, service: android.os.IBinder) {
            importService = (service as ApplicationMigrationService.ApplicationMigrationBinder).getService()
            importService.setImportStateHandler(importStateHandler)

            val state = importService.getState()
            importStateHandler.obtainMessage(state.state, state.progress).sendToTarget()
        }

        override fun onServiceDisconnected(name: android.content.ComponentName) {
            importService.setImportStateHandler(null)
        }
    }

    var isTrackingInProgress = false
    var resultChannel: Channel<Location> = Channel()

    fun startLocationTracking(): Channel<Location> {
        appContext.bindService(Intent(appContext, LocationService.class), )
    }

    fun stopLocationTracking() {
        appContext.unbindService()
    }
}