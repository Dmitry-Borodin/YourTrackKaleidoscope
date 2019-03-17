package com.pet.kaleidoscope.ui.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pet.kaleidoscope.R
import com.pet.kaleidoscope.ui.flickrauth.FlickrAuthClientCallback
import com.pet.kaleidoscope.ui.flickrauth.FlickrVerifierDialog
import com.viewbinder.bindView
import org.koin.android.scope.bindScope
import org.koin.android.scope.getActivityScope


class MainActivity : AppCompatActivity(), MainView {

    private val startStopLink by bindView<TextView>(R.id.activity_main_text)
    private val recycler by bindView<RecyclerView>(R.id.activity_main_recycler)

    private val adapter = PicturesAdapter()
    private val presenter: MainPresenter by getActivityScope().inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        bindScope(getActivityScope())
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
        adapter.items = mutableListOf()
    }

    override fun showPictures(urls: List<String?>) {
        adapter.items = urls.filterNotNull().toMutableList()
    }

    override fun requestAuth(url: String) {
        val callback = object : FlickrAuthClientCallback {
            override fun onSuccessIntercepted(verifier: String) {
                presenter.onAuthRequestedSuccessfully(verifier)
            }
        }

        FlickrVerifierDialog(this, callback).show()
        val intent = Intent(Intent.ACTION_VIEW).also {
            it.data = Uri.parse(url)
        }
        startActivity(intent)
    }

    override fun getActivity(): Activity = this

    override fun showInformationDialog(text: String) {
        AlertDialog.Builder(this)
            .setCancelable(true)
            .setMessage(text)
            .setNeutralButton(R.string.common_ok) { dialog, which -> dialog.dismiss() }
            .create().show()
    }
}
