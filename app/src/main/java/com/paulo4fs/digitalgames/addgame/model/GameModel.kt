package com.paulo4fs.digitalgames.addgame.model

import com.google.gson.annotations.SerializedName

data class GameModel(
    @SerializedName("imageUrl")
    var imageUrl: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("createdAt")
    var createdAt: Int = 0,
    @SerializedName("description")
    var description: String = ""
)