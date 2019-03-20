package com.pet.kaleidoscope.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pet.kaleidoscope.R
import com.pet.kaleidoscope.models.TrackingPoint
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author Dmitry Borodin on 3/10/19.
 */
class PicturesAdapter : RecyclerView.Adapter<PictureViewHolder>() {

    var items: MutableList<TrackingPoint> = mutableListOf()
        set(value) {
            if (field == value) return
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_image, parent, false)
        return PictureViewHolder(view = view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(items[items.lastIndex - position])
    }
}

class PictureViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val imageView = view.findViewById<ImageView>(R.id.item_image_view)
    private val textView = view.findViewById<TextView>(R.id.item_imave_text)

    fun bind(point: TrackingPoint) {
        textView.text = "time = ${point.timestamp.getTimeText()}; location is  ${point.location.latitude} , ${point.location.longitude}"
        if (point.url.isNullOrBlank()) {
            Timber.e("trying to bind empty string url to image view")
            view.visibility = View.INVISIBLE
        } else {
            view.visibility = View.VISIBLE
            Picasso.get()
                .load(point.url)
                .fit()
                .into(imageView)
        }
    }

    private fun Long.getTimeText(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.US)
        val date = Date(this)
        return sdf.format(date)
    }
}