package com.pet.kaleidoscope.data

/**
 * @author Dmitry Borodin on 3/10/19.
 */

interface Repository {
    abstract var oauthFlickrCredentials: FlickrOAuthData?
}

class PreferencesRepository : Repository {

    override var oauthFlickrCredentials: FlickrOAuthData?
        get() = null
        set(value) {}
}