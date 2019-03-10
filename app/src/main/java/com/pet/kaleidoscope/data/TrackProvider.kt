package com.pet.kaleidoscope.data

import com.flickr4java.flickr.REST
import com.flickr4java.flickr.Flickr



/**
 * @author Dmitry Borodin on 2/22/19.
 */
object TrackProvider {
    fun getTracksChannel() = Unit

    fun getFlickrResults() {
        val apiKey = "YOUR_API_KEY"
        val sharedSecret = "YOUR_SHARED_SECRET"
        val flickr = Flickr(apiKey, sharedSecret, REST())
        val testInterface = flickr.testInterface
        val results = testInterface.echo(mapOf())
    }
}