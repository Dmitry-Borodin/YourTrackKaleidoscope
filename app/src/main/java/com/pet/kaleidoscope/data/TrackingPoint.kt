package com.pet.kaleidoscope.data

import android.location.Location

/**
 * @author Dmitry Borodin on 3/17/19.
 */
data class TrackingPoint(val location: Location, val url: String, val timestamp: Long) {
}