package com.paulo4fs.digitalgames.authentication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.paulo4fs.digitalgames.R

class LoginFragment : Fragment() {
    private lateinit var _view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _view = view
        signUpHandler()
        logInHandler()
    }

    private fun logInHandler() {
        val loginBtn = _view.findViewById<MaterialButton>(R.id.mbLoginLogin)
        loginBtn.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun signUpHandler() {
        val signupBtn = _view.findViewById<MaterialButton>(R.id.mbSignupLogin)
        signupBtn.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }
}