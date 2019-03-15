package com.pet.kaleidoscope.data.storage

import android.content.Context

/**
 * @author Dmitry Borodin on 3/10/19.
 */

interface FlickrRepository {
    var oauthFlickrCredentials: FlickrOAuthData?
}

class FlickrRepositoryImpl(appContext: Context, fileName: String = "FlickrRepository") : BasePreferences(appContext, fileName), FlickrRepository {

    override var oauthFlickrCredentials: FlickrOAuthData?
        get() {
            val token = getString(OAUTH_TOKEN, null) ?: return null
            val tokenSecret = getString(OAUTH_TOKEN_SECRET, null) ?: return null
            return FlickrOAuthData(token, tokenSecret)
        }
        set(value) {
            setString(OAUTH_TOKEN, value?.token)
            setString(OAUTH_TOKEN_SECRET, value?.tokenSecret)
        }

    companion object {
        private const val OAUTH_TOKEN = "OAUTH_TOKEN"
        private const val OAUTH_TOKEN_SECRET = "OAUTH_TOKEN_SECRET"
    }
}