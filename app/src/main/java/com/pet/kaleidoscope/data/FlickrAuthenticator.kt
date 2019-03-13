package com.pet.kaleidoscope.data

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.FlickrException
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.RequestContext
import com.flickr4java.flickr.auth.Auth
import com.flickr4java.flickr.auth.Permission
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.model.OAuth1Token
import com.pet.kaleidoscope.Constants
import com.pet.kaleidoscope.decode
import timber.log.Timber

/**
 * @author Dmitry Borodin on 3/13/19.
 */
class FlickrAuthenticator(val repository: Repository) {

    //TODO inject as a dependency
    val flickr = Flickr(Constants.FLICKR_API.decode(), Constants.FLICKR_SECRET.decode(), REST())

    //TODO
    suspend fun hasReadPermissions(): Boolean? {
        val credentials = repository.oauthFlickrCredentials ?: return false
        try {
            val auth = flickr.authInterface.checkToken(credentials.token, credentials.tokenSecret)
            return auth.permission.type >= Permission.READ_TYPE
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false

    }

    /**
     * step 1 of 2 - get url to show webview, to grab verifier token from it
     */
    suspend fun getAuthUrl() {

    }


    /**
     * step 2 of 2 - authorise based on verifier and save auth token
     */
    suspend fun getAuthToken(oauthToken: String, oauthVerifier: String) {
        try {
            val requestToken: OAuth1RequestToken = flickr.authInterface.requestToken
//            val permissionUrl = flickr.authInterface.getAuthorizationUrl(requestToken, Permission.READ) TODO
            val accessToken: OAuth1Token = flickr.authInterface.getAccessToken(requestToken, oauthVerifier)
            val auth = Auth().apply {
                this.token = accessToken.token
                tokenSecret = accessToken.tokenSecret
                permission = Permission.WRITE
            }
            RequestContext.getRequestContext().auth = auth
            flickr.auth = auth
            repository.oauthFlickrCredentials = FlickrOAuthData(accessToken.token, accessToken.tokenSecret)
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