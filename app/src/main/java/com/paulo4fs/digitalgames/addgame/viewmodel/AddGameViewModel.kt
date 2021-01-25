package com.paulo4fs.digitalgames.addgame.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.paulo4fs.digitalgames.addgame.model.GameModel
import com.paulo4fs.digitalgames.utils.AuthUtils.getUserId
import com.paulo4fs.digitalgames.utils.GameUtils

class AddGameViewModel(application: Application) : AndroidViewModel(application) {
    var loading = MutableLiveData<Boolean>()
    var error = MutableLiveData<String>()
    var stateGameRegistered = MutableLiveData<Boolean>()
    var stateImage = MutableLiveData<String>()

    fun createGame(
        gameModel: GameModel
    ) {
        loading.value = true
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference(
            getUserId(getApplication()).toString()
        )

        reference.orderByKey().addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var gameAdded = false

                if (snapshot.hasChildren()) {
                    snapshot.children.forEach {
                        val firebaseResult = it.getValue(GameModel::class.java)
                        if (firebaseResult?.title != null && firebaseResult.title == gameModel.title) {
                            gameAdded = true
                        }
                    }
                }
                if (gameAdded) {
                    errorMessage("The game already exists")
                    loading.value = false
                } else {
                    saveGameFirebase(reference, gameModel)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun saveGameFirebase(reference: DatabaseReference, gameModel: GameModel) {
        Log.d("TAG", "adicionando no firebase")
        val key = reference.push().key

        key?.let {
            reference.child(it).setValue(gameModel)

            reference.child(key).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    loading.value = false
                    Log.d("TAG", "jogo adicionado")
                    stateGameRegistered.value = true
                }

                override fun onCancelled(error: DatabaseError) {
                    loading.value = false
                    Log.d("TAG", "jogo não adicionado")
                    errorMessage("Couldn't add the game")
                }
            })
        }
    }

    fun errorMessage(s: String) {
        error.value = s
    }

    fun updateGamePhoto(
        view: View,
        imageUri: Uri?
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        loading.value = true

        if (imageUri != null && user != null) {
            val database = FirebaseStorage.getInstance()
            val storageReference = database.reference.child(
                "GAME_IMAGE_" +
                        System.currentTimeMillis() +
                        "." +
                        GameUtils.getFileExtension(view, imageUri)
            )

            storageReference.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { taskSnapshot ->
                        loading.value = false
                        Log.d("TAG", taskSnapshot.toString())
                        stateImage.value = taskSnapshot.toString()

                    }.addOnFailureListener {
                        loading.value = false
                        errorMessage("erro ao obter imagem")
                        stateImage.value = ""
                    }
                }
        } else {
            loading.value = false
            stateImage.value = ""
        }
    }
}