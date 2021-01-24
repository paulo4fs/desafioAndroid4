package com.paulo4fs.digitalgames.home.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.paulo4fs.digitalgames.R
import com.squareup.picasso.Picasso

class HomeViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val _imageView = view.findViewById<ImageView>(R.id.ivCoverList)
    private val _titleView = view.findViewById<TextView>(R.id.tvTitleList)
    private val _createdAtView = view.findViewById<TextView>(R.id.tvCreatedAtList)

    fun bind(image: String, title: String, createdAt: Int) {
        _titleView.text = title
        _createdAtView.text = createdAt.toString()
        if (!image.isNullOrEmpty()) {
            Picasso.get().load(image).into(_imageView)
        } else {
            Picasso.get().load(R.drawable.wallpaper).into(_imageView)
        }
    }
}