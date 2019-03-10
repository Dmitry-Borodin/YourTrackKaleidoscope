package com.pet.kaleidoscope.data

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.FlickrException
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.RequestContext
import com.flickr4java.flickr.auth.Auth
import com.flickr4java.flickr.auth.Permission
import com.flickr4java.flickr.photos.GeoData
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.model.Token
import com.pet.kaleidoscope.Constants
import com.pet.kaleidoscope.decode
import timber.log.Timber


/**
 * @author Dmitry Borodin on 2/22/19.
 */
object FlickrProvider {
    val flickr = Flickr(Constants.FLICKR_API.decode(), Constants.FLICKR_SECRET.decode(), REST())

    //TODO
    suspend fun hasReadPermissions(): Boolean? {
        flickr.authInterface.checkToken()
    }

    suspend fun getFlickrPicUrl(): String {
        val geoData = GeoData("48.136553", "11.565598", Flickr.ACCURACY_REGION.toString())

        val flickrPhoto =
            try {
                flickr.photosInterface.geoInterface.photosForLocation(geoData, emptySet(), 0, 0).first()
            } catch (e: FlickrException) {
                Timber.e(e)
                return ""
            }
        return flickrPhoto.medium640Url
    }

    suspend fun autoriseForFlickr(oauthToken: String, oauthVerifier: String) {
        try {
            val accessToken: Token = flickr.authInterface.getAccessToken(oauthToken, oauthVerifier)
            val requestToken: OAuth1RequestToken = flickr.authInterface.requestToken
            val auth = Auth().apply {
                token = requestToken.token
                tokenSecret = requestToken.tokenSecret
                permission = Permission.WRITE
            }
            RequestContext.getRequestContext().auth = auth
            flickr.auth = auth;
            auth = flickr.authInterface.checkToken(accessToken)
            // This token can be used until the user revokes it.
        } catch (e: FlickrException) {
            Timber.d(e)
            e.printStackTrace()
        } catch (e: Exception) {
            Timber.d(e)
            e.printStackTrace()
        }
    }
}