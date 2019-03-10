package com.pet.kaleidoscope.data

/**
 * @author Dmitry Borodin on 3/10/19.
 */

interface Repository {
    abstract var oauthFlickrCredentials: FlickrOAuthData?
}

class PreferencesRepository() : Repository {

    override var oauthFlickrCredentials: FlickrOAuthData?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
}