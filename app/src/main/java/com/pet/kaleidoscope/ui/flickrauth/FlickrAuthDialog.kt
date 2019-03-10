package com.pet.kaleidoscope.ui.flickrauth

import android.app.Dialog
import android.content.Context
import android.webkit.WebView
import com.pet.kaleidoscope.Constants
import com.pet.kaleidoscope.R
import com.viewbinder.bindView

/**
 * @author Dmitry Borodin on 3/10/19.
 */
class FlickrAuthDialog(context: Context, callback: FlickrAuthClientCallback): Dialog(context) {
    val webView by bindView<WebView>(R.id.dialog_auth_web_view)
    
    init {
        setContentView(R.layout.dialog_auth)
        webView.loadUrl(Constants.AUTH_URL)
        webView.webViewClient = FlickrAuthWebViewClient(callback = callback)
        setCancelable(true)
        setTitle(context.getString(R.string.flickr_auth_title))
    }
}