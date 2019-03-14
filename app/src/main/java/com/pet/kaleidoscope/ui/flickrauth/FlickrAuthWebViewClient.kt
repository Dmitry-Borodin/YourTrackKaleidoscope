package com.pet.kaleidoscope.ui.flickrauth

import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient


/**
 * @author Dmitry Borodin on 3/10/19.
 */
class FlickrAuthWebViewClient(val callback: FlickrAuthClientCallback) : WebViewClient() {

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        if (url?.contains("&oauth_verifier") == true) {
            val uri = Uri.parse(url)
            val oauthVerifier = uri.getQueryParameter("oauth_verifier")
            val oauthToken = uri.getQueryParameter("oauth_token")
            requireNotNull(oauthVerifier) {
                "oauthVerifier is null during proper url parsing for auth"
            }
            requireNotNull(oauthToken) {
                "oauthToken is null during proper url parsing for auth"
            }
            callback.onSuccessIntercepted(oauthVerifier)
            //TODO cancel dialog?
        }
    }
}

interface FlickrAuthClientCallback {
    fun onSuccessIntercepted(verifier: String)
}