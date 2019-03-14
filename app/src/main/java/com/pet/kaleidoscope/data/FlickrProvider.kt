package com.pet.kaleidoscope.data

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.FlickrException
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.RequestContext
import com.flickr4java.flickr.auth.Auth
import com.flickr4java.flickr.auth.Permission
import com.flickr4java.flickr.photos.GeoData
import com.pet.kaleidoscope.Constants
import com.pet.kaleidoscope.decode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


/**
 * @author Dmitry Borodin on 2/22/19.
 */
class FlickrProvider(private val repository: Repository) {
    private val flickr = Flickr(Constants.FLICKR_API.decode(), Constants.FLICKR_SECRET.decode(), REST())

    init {
        val auth = Auth().apply {
            this.token = Constants.OAUTH_TOKEN.decode()
            tokenSecret = Constants.OAUTH_TOKEN_SECRET.decode()
            permission = Permission.WRITE
        }
        RequestContext.getRequestContext().auth = auth
    }

    suspend fun getFlickrPicUrl(): String = withContext(Dispatchers.IO) {
        val geoData = GeoData("48.136553", "11.565598", Flickr.ACCURACY_REGION.toString())

        val flickrPhoto =
            try {
                flickr.photosInterface.geoInterface.photosForLocation(geoData, emptySet(), 0, 0).first()
            } catch (e: FlickrException) {
                Timber.e(e)
                return@withContext ""
            }
        return@withContext flickrPhoto.medium640Url
    }


}