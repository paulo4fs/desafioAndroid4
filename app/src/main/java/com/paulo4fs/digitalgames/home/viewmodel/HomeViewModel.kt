package com.paulo4fs.digitalgames.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.paulo4fs.digitalgames.addgame.model.GameModel
import com.paulo4fs.digitalgames.utils.AuthUtils.getUserId
import com.paulo4fs.digitalgames.utils.Constants.GAMES_PATH

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    var loading = MutableLiveData<Boolean>()
    var error = MutableLiveData<String>()
    var stateList = MutableLiveData<List<GameModel>>()

    fun getGames() {
        loading.value = true
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference(
            getUserId(getApplication()).toString()
        )

        reference.orderByKey().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val games = mutableListOf<GameModel>()
                loading.value = false
                snapshot.children.forEach {
                    val game = it.getValue(GameModel::class.java)
                    game?.let { games.add(it) }
                }
                stateList.value = games
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}