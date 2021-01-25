package com.paulo4fs.digitalgames.home.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.paulo4fs.digitalgames.addgame.model.GameModel
import com.paulo4fs.digitalgames.utils.AuthUtils.getUserId


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    var loading = MutableLiveData<Boolean>()
    var error = MutableLiveData<String>()
    var stateList = MutableLiveData<List<GameModel>>()

    fun getListGames() {
        loading.value = true
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference(
            getUserId(getApplication()).toString()
        )
        Log.d("TAG", "get games")
        reference.orderByKey().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val games = mutableListOf<GameModel>()
                loading.value = false
                snapshot.children.forEach {
                    val game = it.getValue(GameModel::class.java)
                    game?.let { games.add(it) }
                }
                Log.d("TAG", "Home: on Data change")
                stateList.value = games
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "Home: cancelled")
                TODO("Not yet implemented")
            }
        })
    }

    fun queryFirebase(searchText: String) {
        Log.d("TAG", searchText)

        val databaseReference =
            FirebaseDatabase.getInstance().getReference(getUserId(getApplication()).toString())

        val query: Query =
            databaseReference.orderByChild("title").startAt(searchText).endAt("${searchText}\uf8ff")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("TAG", "onDataChange")
                if (dataSnapshot.exists()) {
                    Log.d("TAG", "dataSnapshot.exists")
                    // dataSnapshot is the "issue" node with all children with id 0
                    val game = mutableListOf<GameModel>()
                    for (data in dataSnapshot.children) {
                        // do something with the individual "issues"
                        val gameData = data.getValue(GameModel::class.java)
                        gameData?.let { game.add(it) }
                    }
                    stateList.value = game
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", "onCancelled")
            }
        })
    }
}