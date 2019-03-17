package com.pet.kaleidoscope.logic

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.RequestContext
import com.flickr4java.flickr.auth.Auth
import com.flickr4java.flickr.auth.Permission
import com.flickr4java.flickr.photos.GeoData
import com.flickr4java.flickr.photos.SearchParameters
import com.pet.kaleidoscope.Constants
import com.pet.kaleidoscope.models.LatLong
import com.pet.kaleidoscope.logic.storage.FlickrRepository
import com.pet.kaleidoscope.decode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * @author Dmitry Borodin on 2/22/19.
 */
class FlickrUrlProvider(private val repository: FlickrRepository) {
    private val flickr = Flickr(Constants.FLICKR_API.decode(), Constants.FLICKR_SECRET.decode(), REST())

    init {
        val auth = Auth().apply {
            val credentials = repository.oauthFlickrCredentials ?: return@apply
            this.token = credentials.token.decode()
            tokenSecret = credentials.tokenSecret.decode()
            permission = Permission.READ
        }
        RequestContext.getRequestContext().auth = auth
    }

    suspend fun getFlickrPicUrl(latLong: LatLong): String? = withContext(Dispatchers.IO) {

        val geoData = GeoData(latLong.longitude.toString(), latLong.latitude.toString(), Flickr.ACCURACY_REGION.toString())
        val searchParameters = SearchParameters().apply {
            latitude = geoData.latitude.toString()
            longitude = geoData.longitude.toString()
        }
        val searchPhoto = flickr.photosInterface.search(searchParameters, 1,1).firstOrNull()
        return@withContext searchPhoto?.medium640Url
    }


}