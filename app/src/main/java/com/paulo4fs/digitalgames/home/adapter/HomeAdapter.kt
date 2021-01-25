package com.paulo4fs.digitalgames.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.paulo4fs.digitalgames.R
import com.paulo4fs.digitalgames.addgame.model.GameModel
import com.squareup.picasso.Picasso
import java.util.*

class HomeAdapter(
    private val _gamesList: MutableList<GameModel>,
    private val listener: (GameModel) -> Unit
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.game_list_item, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = _gamesList[position]

        val image = item.imageUrl
        val title = item.title
        val createdAt = item.createdAt

        holder.bind(image, title, createdAt)
        holder.itemView.setOnClickListener { listener(item) }
    }

    override fun getItemCount() = _gamesList.size

    class HomeViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val _imageView = view.findViewById<ImageView>(R.id.ivCoverList)
        private val _titleView = view.findViewById<TextView>(R.id.tvTitleList)
        private val _createdAtView = view.findViewById<TextView>(R.id.tvCreatedAtList)

        fun bind(image: String, title: String, createdAt: Int) {
            _titleView.text = title.capitalize(Locale.ROOT)
            _createdAtView.text = createdAt.toString()
            if (!image.isEmpty()) {
                Picasso.get().load(image).into(_imageView)
            } else {
                Picasso.get().load(R.drawable.placeholder).into(_imageView)
            }
        }
    }
}