package com.pet.kaleidoscope.models

import android.location.Location

/**
 * @author Dmitry Borodin on 3/17/19.
 */
data class LatLong(val latitude: Double, val longitude: Double)

fun Location.toLatLong() = LatLong(latitude = this.latitude, longitude = this.longitude)