package com.pet.kaleidoscope.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pet.kaleidoscope.R

/**
 * @author Dmitry Borodin on 3/10/19.
 */
class PicturesAdapter : RecyclerView.Adapter<PictureViewHolder>() {

    var items: MutableList<String> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_item_image, parent, false)
        return PictureViewHolder(view = view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class PictureViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(url: String) {
        //TODO
    }
}