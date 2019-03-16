package com.pet.kaleidoscope.logic

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.RequestContext
import com.flickr4java.flickr.auth.Auth
import com.flickr4java.flickr.auth.Permission
import com.flickr4java.flickr.photos.GeoData
import com.flickr4java.flickr.photos.SearchParameters
import com.pet.kaleidoscope.Constants
import com.pet.kaleidoscope.logic.storage.FlickrRepository
import com.pet.kaleidoscope.decode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * @author Dmitry Borodin on 2/22/19.
 */
class FlickrProvider(private val repository: FlickrRepository) {
    private val flickr = Flickr(Constants.FLICKR_API.decode(), Constants.FLICKR_SECRET.decode(), REST())

    init {
        val auth = Auth().apply {
            this.token = Constants.OAUTH_TOKEN.decode()
            tokenSecret = Constants.OAUTH_TOKEN_SECRET.decode()
            permission = Permission.WRITE
        }
        RequestContext.getRequestContext().auth = auth
    }

    suspend fun getFlickrPicUrl(): String? = withContext(Dispatchers.IO) {
        val geoData = GeoData("48.136553", "11.565598", Flickr.ACCURACY_REGION.toString())

        val searchParameters = SearchParameters().apply {
            latitude = geoData.latitude.toString()
            longitude = geoData.longitude.toString()
        }
        val searchPhoto = flickr.photosInterface.search(searchParameters, 1,1).firstOrNull()
        return@withContext searchPhoto?.medium640Url
    }


}