package com.pet.kaleidoscope.ui.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pet.kaleidoscope.R
import com.viewbinder.bindView

class MainActivity : AppCompatActivity(), MainView {

    private val startStopLink by bindView<TextView>(R.id.activity_main_text)
    private val recycler by bindView<RecyclerView>(R.id.activity_main_recycler)

    private val adapter = PicturesAdapter()
    private val presenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.start(this)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        startStopLink.setOnClickListener { presenter.onStartStopBottonClicked() }
    }

    override fun onDestroy() {
        presenter.stop()
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
}
