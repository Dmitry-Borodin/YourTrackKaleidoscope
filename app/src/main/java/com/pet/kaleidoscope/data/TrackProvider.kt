package com.pet.kaleidoscope.data

import android.util.Base64
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.Flickr
import com.pet.kaleidoscope.Constants


/**
 * @author Dmitry Borodin on 2/22/19.
 */
object TrackProvider {
    fun getTracksChannel() = Unit

    fun getFlickrResults() {
        val apiKey = Constants.FLICKR_API
        val sharedSecret = Constants.FLICKR_SECRET
        val flickr = Flickr(apiKey, sharedSecret, REST())
        val testInterface = flickr.testInterface
        val results = testInterface.echo(mapOf())
    }
}