package com.pet.kaleidoscope.logic

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.FlickrException
import com.flickr4java.flickr.REST
import com.flickr4java.flickr.RequestContext
import com.flickr4java.flickr.auth.Auth
import com.flickr4java.flickr.auth.Permission
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.model.OAuth1Token
import com.pet.kaleidoscope.Constants
import com.pet.kaleidoscope.logic.storage.FlickrOAuthData
import com.pet.kaleidoscope.logic.storage.FlickrRepository
import com.pet.kaleidoscope.decode
import com.pet.kaleidoscope.encode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * @author Dmitry Borodin on 3/13/19.
 */
class FlickrAuthenticator(private val repository: FlickrRepository) {

    //TODO inject as a dependency
    private val flickr = Flickr(Constants.FLICKR_API.decode(), Constants.FLICKR_SECRET.decode(), REST())
    private lateinit var requestToken: OAuth1RequestToken

    //TODO
    suspend fun hasReadPermissions(): Boolean? = withContext(Dispatchers.IO) {
        val credentials = repository.oauthFlickrCredentials ?: return@withContext false
        Timber.d("loaded credentials, $credentials")
        try {
            val auth = flickr.authInterface.checkToken(credentials.token, credentials.tokenSecret)
            Timber.d("permission loaded, have at least read = ${auth.permission.type >= Permission.READ_TYPE}")
            return@withContext auth.permission.type >= Permission.READ_TYPE
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return@withContext false
    }

    /**
     * step 1 of 2 - get url to show webview, to grab verifier token from it
     */
    suspend fun getAuthUrl(): String = withContext(Dispatchers.IO) {
        requestToken = flickr.authInterface.getRequestToken(null)
        return@withContext flickr.authInterface.getAuthorizationUrl(requestToken, Permission.READ)
    }


    /**
     * step 2 of 2 - authorise based on verifier and save auth token
     */
    suspend fun getAuthToken(oauthVerifier: String) = withContext(Dispatchers.IO) {
        try {
            val accessToken: OAuth1Token = flickr.authInterface.getAccessToken(requestToken, oauthVerifier)

            repository.oauthFlickrCredentials = FlickrOAuthData(accessToken.token, accessToken.tokenSecret)
            val auth = Auth().apply {
                this.token = accessToken.token
                tokenSecret = accessToken.tokenSecret
                permission = Permission.READ
            }
            RequestContext.getRequestContext().auth = auth
            flickr.auth = auth
            repository.oauthFlickrCredentials = FlickrOAuthData(accessToken.token.encode(), accessToken.tokenSecret.encode())
            // This token can be used until the user revokes it.
        } catch (e: FlickrException) {
            Timber.d(e)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }
}