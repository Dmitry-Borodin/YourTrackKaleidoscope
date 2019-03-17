package com.pet.kaleidoscope.logic

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.pet.kaleidoscope.Constants
import com.pet.kaleidoscope.data.TrackingPoint
import com.pet.kaleidoscope.data.toLatLong
import com.pet.kaleidoscope.service.LocationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutionException

/**
 * @author Dmitry Borodin on 3/14/19.
 *
 * fixme it is currently mixing responsibilities of business logic and use-case
 */
class LocationProvider(
    private val appContext: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient, //supporting gms only since it's a pet project
    private val flickrUrlProvider: FlickrUrlProvider
) {

    var resultChannel: Channel<TrackingPoint> = Channel()
    private val locationRequest: LocationRequest = createLocationRequest()
    private val locationUpdateCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?): Unit = GlobalScope.launch {
            val location = locationResult?.lastLocation?.toLatLong() ?: return@launch
            val timeStamp = System.currentTimeMillis()
            val url = flickrUrlProvider.getFlickrPicUrl(location)
            resultChannel.send(TrackingPoint(location = location, timestamp = timeStamp, url = url))
        }.ignore()
    }
    var isTrackingInProgress = false

    private fun Any.ignore(): Unit = Unit

    fun startLocationTracking(): Channel<TrackingPoint> {
        startForegroundService()
        resultChannel = Channel()
        isTrackingInProgress = true
        if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationUpdateCallback,
                Looper.getMainLooper()
            )
        }
        return resultChannel
    }

    fun stopLocationTracking() {
        isTrackingInProgress = false
        fusedLocationProviderClient.removeLocationUpdates(locationUpdateCallback)
        resultChannel.close()
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
            interval = Constants.GPS_REQUEST_INTERVAL_MS.toLong()
            smallestDisplacement = Constants.MINIMIM_DISTANCE_THRESHOLD_IN_METERS.toFloat()
        }
    }

    suspend fun isGpsUsable(): Boolean = withContext(Dispatchers.IO) {
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()
        val client: SettingsClient = LocationServices.getSettingsClient(appContext)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(settingsRequest)
        try {
            Tasks.await(task) //TODO create coroutines wrapper for tasks API
        } catch (e: ExecutionException) {
            return@withContext false
        }
        catch (e: ResolvableApiException) {
            return@withContext false
        }
        return@withContext task.result?.locationSettingsStates?.isGpsUsable == true
    }
}