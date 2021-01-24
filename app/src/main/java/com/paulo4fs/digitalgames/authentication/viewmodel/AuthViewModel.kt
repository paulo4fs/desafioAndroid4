package com.paulo4fs.digitalgames.authentication.viewmodel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.paulo4fs.digitalgames.utils.AuthUtils

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    var loading = MutableLiveData<Boolean>()
    var error = MutableLiveData<String>()
    var stateLogin = MutableLiveData<Boolean>()
    var stateRegister = MutableLiveData<Boolean>()
    var stateRegistered = MutableLiveData<Boolean>()

    fun checkUser() {
        val user = FirebaseAuth.getInstance().currentUser
        stateRegistered.value = user != null
    }

    fun registerUser(
        userName: String,
        userEmail: String,
        userPassword: String
    ) {
        loading.value = true

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                loading.value = false

                val profileUpdateName = userProfileChangeRequest {
                    displayName = userName
                }

                when {
                    task.isSuccessful -> {
                        Log.d("TAG", "user created")
                        stateRegister.value = true
                    }
                    else -> {
                        Log.d("TAG", "failed to create user")
                        stateRegister.value = false
                        errorMessage("Failed to create user")
                    }
                }
            }
    }

    fun loginUser(
        activity: Activity,
        userEmail: String,
        userPassword: String
    ) {
        loading.value = true
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                loading.value = false

                when {
                    task.isSuccessful -> {
                        Log.d("TAG", "Login realizado com sucesso")
                        val user = FirebaseAuth.getInstance().currentUser
                        AuthUtils.saveUserId(activity, user?.uid.toString())
                        stateLogin.value = true
                    }
                    else -> {
                        Log.d("TAG", "Problemas de login")
                        errorMessage("Authentication failed")
                        stateLogin.value = false
                    }
                }
            }
    }

    fun errorMessage(s: String) {
        error.value = s
    }
}