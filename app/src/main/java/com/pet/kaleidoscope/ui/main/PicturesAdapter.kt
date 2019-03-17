package com.pet.kaleidoscope.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.pet.kaleidoscope.R
import com.squareup.picasso.Picasso
import com.viewbinder.bindView
import timber.log.Timber

/**
 * @author Dmitry Borodin on 3/10/19.
 */
class PicturesAdapter : RecyclerView.Adapter<PictureViewHolder>() {

    var items: MutableList<String> = mutableListOf()
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

    fun bind(url: String) {
        if (url.isEmpty()) {
            Timber.e("trying to bind empty string url to image view")
            view.visibility = View.INVISIBLE
        } else {
            view.visibility = View.VISIBLE
            Picasso.get()
                .load(url)
                .fit()
                .into(imageView)
        }
    }
}