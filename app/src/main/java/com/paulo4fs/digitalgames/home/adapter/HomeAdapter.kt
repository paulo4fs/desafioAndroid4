package com.paulo4fs.digitalgames.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paulo4fs.digitalgames.R
import com.paulo4fs.digitalgames.addgame.model.GameModel
import com.paulo4fs.digitalgames.home.adapter.HomeViewHolder

class HomeAdapter(
    private val _gamesList: MutableList<GameModel>,
    private val listener: (GameModel) -> Unit
) : RecyclerView.Adapter<HomeViewHolder>() {
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
}