package com.pet.kaleidoscope.ui.flickrauth

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.webkit.WebView
import com.pet.kaleidoscope.R
import com.viewbinder.bindView

/**
 * @author Dmitry Borodin on 3/10/19.
 */
class FlickrAuthWebViewDialog(context: Context, private val url: String, private val callback: FlickrAuthClientCallback) :
    Dialog(context) {
    private val webView by bindView<WebView>(R.id.dialog_auth_web_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_auth_webview)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        webView.webViewClient = FlickrAuthWebViewClient(callback = callback)
        setCancelable(true)
        setTitle(context.getString(R.string.flickr_auth_title))
    }
}