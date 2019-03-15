package com.pet.kaleidoscope.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pet.kaleidoscope.R
import com.pet.kaleidoscope.ui.flickrauth.FlickrAuthClientCallback
import com.pet.kaleidoscope.ui.flickrauth.FlickrVerifierDialog
import com.viewbinder.bindView


class MainActivity : AppCompatActivity(), MainView {

    private val startStopLink by bindView<TextView>(R.id.activity_main_text)
    private val recycler by bindView<RecyclerView>(R.id.activity_main_recycler)

    private val adapter = PicturesAdapter()
    private val presenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        presenter.onAttach(this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        startStopLink.setOnClickListener { presenter.onStartStopButtonClicked() }
    }

    override fun onDestroy() {
        presenter.onDetouch()
        super.onDestroy()
    }

    override fun setStateRunning() {
        startStopLink.text = getString(R.string.common_stop)
    }

    override fun setStateStopped() {
        startStopLink.text = getString(R.string.common_start)
    }

    override fun showPictures(urls: List<String>) {
        adapter.items = urls.toMutableList()
    }

    override fun requestAuth(url: String) {
        val callback = object : FlickrAuthClientCallback {
            override fun onSuccessIntercepted(verifier: String) {
                presenter.onAuthRequestedSuccessfully(verifier)
            }
        }
//        FlickrAuthWebViewDialog(this, url, callback).show()

        FlickrVerifierDialog(this, callback).show()
        val intent = Intent(Intent.ACTION_VIEW).also {
            it.data = Uri.parse(url)
        }
        startActivity(intent)
    }
}
